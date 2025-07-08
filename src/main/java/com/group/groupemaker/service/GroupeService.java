package com.group.groupemaker.service;

import com.group.groupemaker.dto.GroupeDTO;
import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.model.Groupe;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Personne;
import com.group.groupemaker.model.Historique;
import com.group.groupemaker.repository.GroupeRepository;
import com.group.groupemaker.repository.ListeRepository;
import com.group.groupemaker.repository.PersonneRepository;
import com.group.groupemaker.repository.HistoriqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupeService {

    private final GroupeRepository groupeRepository;
    private final ListeRepository listeRepository;
    private final PersonneRepository personneRepository;
    private final GroupeAleatoireService groupeAleatoireService;
    private final HistoriqueRepository historiqueRepository;

    public GroupeService(GroupeRepository groupeRepository, ListeRepository listeRepository, 
                        PersonneRepository personneRepository, GroupeAleatoireService groupeAleatoireService,
                        HistoriqueRepository historiqueRepository) {
        this.groupeRepository = groupeRepository;
        this.listeRepository = listeRepository;
        this.personneRepository = personneRepository;
        this.groupeAleatoireService = groupeAleatoireService;
        this.historiqueRepository = historiqueRepository;
    }

    public List<GroupeDTO> getGroupesParListe(Long listeId) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        return groupeRepository.findByListe(liste)
                .stream()
                .map(groupe -> {
                    GroupeDTO dto = new GroupeDTO(
                            groupe.getId(), groupe.getNom(), groupe.getCriteres(),
                            groupe.getDateCreation(), listeId);

                    dto.setPersonnes(
                            groupe.getPersonnes().stream()
                                    .map(p -> new PersonneDTO(
                                            p.getId(), p.getNom(), p.getGenre(), p.getAisanceFr(), p.getAncienDwwm(),
                                            p.getNiveauTechnique(), p.getProfil(), p.getAge(), listeId))
                                    .collect(Collectors.toList())
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public GroupeDTO creerGroupe(GroupeDTO dto, Long listeId) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        Groupe groupe = new Groupe();
        groupe.setNom(dto.getNom());
        groupe.setCriteres(dto.getCriteres());
        groupe.setListe(liste);

        // Associer les personnes par leur ID
        List<Personne> personnes = dto.getPersonnes().stream()
                .map(pdto -> personneRepository.findById(pdto.getId())
                        .orElseThrow(() -> new RuntimeException("Personne non trouvée : " + pdto.getId())))
                .collect(Collectors.toList());

        groupe.setPersonnes(personnes);
        Groupe saved = groupeRepository.save(groupe);

        return new GroupeDTO(saved.getId(), saved.getNom(), saved.getCriteres(), saved.getDateCreation(), listeId);
    }

    public void supprimerGroupe(Long id) {
        groupeRepository.deleteById(id);
    }
}

