package com.group.groupemaker.service;

import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Personne;
import com.group.groupemaker.repository.ListeRepository;
import com.group.groupemaker.repository.PersonneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonneService {

    private final PersonneRepository personneRepository;
    private final ListeRepository listeRepository;

    public PersonneService(PersonneRepository personneRepository, ListeRepository listeRepository) {
        this.personneRepository = personneRepository;
        this.listeRepository = listeRepository;
    }

    public List<PersonneDTO> getPersonnesParListe(Long listeId) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        return liste.getPersonnes().stream()
                .map(p -> new PersonneDTO(
                        p.getId(), p.getNom(), p.getGenre(), p.getAisanceFr(), p.getAncienDwwm(),
                        p.getNiveauTechnique(), p.getProfil(), p.getAge(), listeId))
                .collect(Collectors.toList());
    }

    public PersonneDTO ajouterPersonne(PersonneDTO dto, Long listeId) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        Personne personne = new Personne(
                dto.getNom(), dto.getGenre(), dto.getAisanceFr(), dto.isAncienDwwm(),
                dto.getNiveauTechnique(), dto.getProfil(), dto.getAge(), liste);

        Personne saved = personneRepository.save(personne);

        return new PersonneDTO(
                saved.getId(), saved.getNom(), saved.getGenre(), saved.getAisanceFr(), saved.getAncienDwwm(),
                saved.getNiveauTechnique(), saved.getProfil(), saved.getAge(), listeId);
    }

    public void supprimerPersonne(Long id) {
        personneRepository.deleteById(id);
    }
}
