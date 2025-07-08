package com.group.groupemaker.controller;

import com.group.groupemaker.dto.GroupeDTO;
import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Groupe;
import com.group.groupemaker.model.Personne;
import com.group.groupemaker.model.Historique;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.repository.ListeRepository;
import com.group.groupemaker.repository.GroupeRepository;
import com.group.groupemaker.repository.HistoriqueRepository;
import com.group.groupemaker.service.GroupeAleatoireService;
import com.group.groupemaker.service.GroupeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groupes")
public class GroupeController {

    private final GroupeService groupeService;
    private final UtilisateurRepository utilisateurRepository;
    private final GroupeAleatoireService groupeAleatoireService;
    private final ListeRepository listeRepository;
    private final GroupeRepository groupeRepository;
    private final HistoriqueRepository historiqueRepository;

    public GroupeController(GroupeService groupeService, UtilisateurRepository utilisateurRepository, 
                           GroupeAleatoireService groupeAleatoireService, ListeRepository listeRepository,
                           GroupeRepository groupeRepository, HistoriqueRepository historiqueRepository) {
        this.groupeService = groupeService;
        this.utilisateurRepository = utilisateurRepository;
        this.groupeAleatoireService = groupeAleatoireService;
        this.listeRepository = listeRepository;
        this.groupeRepository = groupeRepository;
        this.historiqueRepository = historiqueRepository;
    }

    @GetMapping("/liste/{listeId}")
    public List<GroupeDTO> getGroupesParListe(@PathVariable Long listeId) {
        return groupeService.getGroupesParListe(listeId);
    }

    @PostMapping("/liste/{listeId}")
    public GroupeDTO creerGroupe(@RequestBody GroupeDTO dto, @PathVariable Long listeId, Authentication auth) {
        // Optionnel : tu peux vérifier que l’utilisateur connecté possède bien la liste
        getUtilisateurConnecte(auth); // juste pour forcer la vérification
        return groupeService.creerGroupe(dto, listeId);
    }

    @DeleteMapping("/{id}")
    public void supprimerGroupe(@PathVariable Long id) {
        groupeService.supprimerGroupe(id);
    }

    @PostMapping("/liste/{listeId}/aleatoire")
    public ResponseEntity<Map<String, Object>> genererGroupesAleatoires(
            @PathVariable Long listeId,
            @RequestBody Map<String, Object> request,
            Authentication auth) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Récupération des paramètres
            int nombreGroupes = (Integer) request.get("nombreGroupes");
            List<String> nomsGroupes = (List<String>) request.get("nomsGroupes");
            Map<String, Boolean> criteres = (Map<String, Boolean>) request.get("criteres");
            
            // Vérification utilisateur
            Utilisateur utilisateur = getUtilisateurConnecte(auth);
            
            // Récupération de la liste
            Liste liste = listeRepository.findById(listeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Liste non trouvée"));
            
            // Vérification des droits
            if (!liste.getUtilisateur().getId().equals(utilisateur.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès refusé à cette liste");
            }
            
            // Configuration des critères
            GroupeAleatoireService.CriteresGeneration criteresGeneration = new GroupeAleatoireService.CriteresGeneration();
            criteresGeneration.setMixerGenres(criteres.getOrDefault("mixerGenres", false));
            criteresGeneration.setMixerAges(criteres.getOrDefault("mixerAges", false));
            criteresGeneration.setMixerNiveauxTechniques(criteres.getOrDefault("mixerNiveauxTechniques", false));
            criteresGeneration.setMixerAisanceFrancais(criteres.getOrDefault("mixerAisanceFrancais", false));
            criteresGeneration.setMixerAnciensDWWM(criteres.getOrDefault("mixerAnciensDWWM", false));
            criteresGeneration.setMixerProfils(criteres.getOrDefault("mixerProfils", false));
            
            // Génération des groupes
            List<List<Personne>> groupesGeneres = groupeAleatoireService.genererGroupesAleatoires(
                    liste.getPersonnes(), nombreGroupes, criteresGeneration, liste);
            
            // Création des groupes en base
            List<GroupeDTO> groupesDTO = new ArrayList<>();
            for (int i = 0; i < groupesGeneres.size(); i++) {
                String nomGroupe = i < nomsGroupes.size() ? nomsGroupes.get(i) : "Groupe " + (i + 1);
                
                Groupe groupe = new Groupe();
                groupe.setNom(nomGroupe);
                groupe.setCriteres(criteres.toString());
                groupe.setListe(liste);
                groupe.setPersonnes(groupesGeneres.get(i));
                
                Groupe savedGroupe = groupeRepository.save(groupe);
                
                GroupeDTO dto = new GroupeDTO(savedGroupe.getId(), savedGroupe.getNom(), 
                        savedGroupe.getCriteres(), savedGroupe.getDateCreation(), listeId);
                
                dto.setPersonnes(savedGroupe.getPersonnes().stream()
                        .map(p -> new PersonneDTO(p.getId(), p.getNom(), p.getGenre(), 
                                p.getAisanceFr(), p.getAncienDwwm(), p.getNiveauTechnique(), 
                                p.getProfil(), p.getAge(), listeId))
                        .toList());
                
                groupesDTO.add(dto);
            }
            
            // Enregistrement dans l'historique
            Historique historique = new Historique();
            historique.setListe(liste);
            historique.setUtilisateur(utilisateur);
            historique.setNomListe(liste.getNom());
            historique.setGroupes(groupesDTO.stream()
                    .map(dto -> groupeRepository.findById(dto.getId()).orElse(null))
                    .toList());
            
            historiqueRepository.save(historique);
            
            response.put("success", true);
            response.put("data", groupesDTO);
            response.put("message", "Groupes générés avec succès");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erreur lors de la génération des groupes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Utilisateur getUtilisateurConnecte(Authentication auth) {
        return utilisateurRepository.findByEmail(auth.getName())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
    }
}
