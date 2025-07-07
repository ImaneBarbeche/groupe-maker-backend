package com.group.groupemaker.service;

import com.group.groupemaker.dto.ListeDTO;
import com.group.groupemaker.dto.PersonneDTO;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Personne;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.ListeRepository;
import com.group.groupemaker.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListeService {

    private final ListeRepository listeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public ListeService(ListeRepository listeRepository, UtilisateurRepository utilisateurRepository) {
        this.listeRepository = listeRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<ListeDTO> getListesByUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return listeRepository.findByUtilisateur(utilisateur)
                .stream()
                .map(liste -> new ListeDTO(liste.getId(), liste.getNom()))
                .collect(Collectors.toList());
    }

    public ListeDTO createListe(ListeDTO dto, Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Liste liste = new Liste(dto.getNom(), utilisateur);
        Liste saved = listeRepository.save(liste);

        return new ListeDTO(saved.getId(), saved.getNom());
    }

    public void supprimerListe(Long id) {
        Liste liste = listeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        listeRepository.delete(liste);
    }

    public void ajouterPersonnesAListe(Long listeId, List<PersonneDTO> personnes) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        for (PersonneDTO personneDTO : personnes) {
            Personne personne = new Personne(
                    personneDTO.getNom(),
                    personneDTO.getGenre(),
                    personneDTO.getAisanceFr(),
                    personneDTO.isAncienDwwm(),
                    personneDTO.getNiveauTechnique(),
                    personneDTO.getProfil(),
                    personneDTO.getAge(),
                    liste
            );
            liste.getPersonnes().add(personne);
        }

        listeRepository.save(liste);
    }

    public void supprimerPersonne(Long listeId, Long personneId) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        Personne personne = liste.getPersonnes().stream()
                .filter(p -> p.getId().equals(personneId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Personne non trouvée dans la liste"));

        liste.getPersonnes().remove(personne);
        listeRepository.save(liste);
    }

    public PersonneDTO modifierPersonne(Long listeId, Long personneId, PersonneDTO dto) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        Personne personne = liste.getPersonnes().stream()
                .filter(p -> p.getId().equals(personneId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Personne non trouvée dans la liste"));

        personne.setNom(dto.getNom());
        personne.setGenre(dto.getGenre());
        personne.setAisanceFr(dto.getAisanceFr());
        personne.setAncienDwwm(dto.isAncienDwwm());
        personne.setNiveauTechnique(dto.getNiveauTechnique());
        personne.setProfil(dto.getProfil());
        personne.setAge(dto.getAge());

        listeRepository.save(liste);

        return new PersonneDTO(
                personne.getId(),
                personne.getNom(),
                personne.getGenre(),
                personne.getAisanceFr(),
                personne.getAncienDwwm(),
                personne.getNiveauTechnique(),
                personne.getProfil(),
                personne.getAge()
        );
    }

    public ListeDTO modifierListe(Long listeId, ListeDTO dto) {
        Liste liste = listeRepository.findById(listeId)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        liste.setNom(dto.getNom());
        liste.setDescription(dto.getDescription());

        Liste updatedListe = listeRepository.save(liste);

        return new ListeDTO(updatedListe.getId(), updatedListe.getNom(), updatedListe.getDescription());
    }
}