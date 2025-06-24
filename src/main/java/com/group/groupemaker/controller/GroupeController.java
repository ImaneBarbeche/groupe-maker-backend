package com.group.groupemaker.controller;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.group.groupemaker.model.Formateur;
import com.group.groupemaker.model.Groupe;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Personne;
import com.group.groupemaker.repository.FormateurRepository;
import com.group.groupemaker.repository.GroupeRepository;
import com.group.groupemaker.repository.ListeRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/groupes")
public class GroupeController {
    private final GroupeRepository groupeRepository;
    private final FormateurRepository formateurRepository;
    private final ListeRepository listeRepository;

    public GroupeController(GroupeRepository groupeRepository,
            FormateurRepository formateurRepository,
            ListeRepository listeRepository) {
        this.groupeRepository = groupeRepository;
        this.formateurRepository = formateurRepository;
        this.listeRepository = listeRepository;
    }

    // on répond à une requête GET avec la liste des groupes
    @GetMapping
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }

    // récupérer les groupes par id
    @GetMapping("/{id}")
    public Groupe getGroupeById(@PathVariable Long id) {
        return groupeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupe non trouvé"));
    }

    @PostMapping
    public Groupe createGroupe(@RequestBody Groupe groupe) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Formateur formateur = formateurRepository.findByUtilisateurEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Formateur non trouvé"));

        groupe.setFormateur(formateur);
        return groupeRepository.save(groupe);
    }

    @PostMapping("/personnes-par-liste")
    public ResponseEntity<?> genererGroupes(@RequestBody Map<String, Object> data) {
        Long idListe = Long.valueOf(data.get("idListe").toString());

        Optional<Liste> optListe = listeRepository.findById(idListe);
        if (optListe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Liste introuvable avec id = " + idListe);
        }

        Liste liste = optListe.get();
        List<Personne> personnes = liste.getPersonnes();
        return ResponseEntity.ok(personnes);
    }

    /**
     * Modifie un groupe existant si l'utilisateur connecté est le formateur qui l'a
     * créé.
     * Vérifie l'existence du groupe et la correspondance du formateur avant
     * d'appliquer les modifications.
     * Retourne le groupe mis à jour.
     */
    @PutMapping("/{id}")
    public Groupe updateGroupeById(@PathVariable Long id, @RequestBody Groupe groupe) {

        // 1. Récupérer le formateur connecté
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Formateur formateur = formateurRepository.findByUtilisateurEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Formateur non trouvé"));

        // 2. Récupérer le groupe existant et vérifier s’il appartient au formateur
        Groupe existing = groupeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupe non trouvé"));

        if (!existing.getFormateur().getId().equals(formateur.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez modifier que vos propres groupes");
        }

        // 3. Appliquer les modifications
        existing.setNom(groupe.getNom());
        existing.setCriteres(groupe.getCriteres());

        // 4. Enregistrer et retourner le groupe mis à jour
        return groupeRepository.save(existing);

    }

    /**
     * Supprime un groupe à partir de son identifiant et seulement en tant que
     * formateur
     * Si le groupe est trouvé, il est supprimé de la base et retournée en
     * réponse.
     * Sinon, la méthode retourne une erreur
     */
    @DeleteMapping("/{id}")
    public Groupe deleteGroupeById(@PathVariable Long id) {
        // 1. Récupérer le formateur connecté via son email
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Formateur formateur = formateurRepository.findByUtilisateurEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Formateur non trouvé"));

        // 2. Vérifier que le groupe appartient bien à ce formateur
        Groupe existing = groupeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Groupe non trouvé"));

        if (!existing.getFormateur().getId().equals(formateur.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez supprimer que vos propres groupes");
        }

        groupeRepository.deleteById(id);
        return existing;

    }
}
