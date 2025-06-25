package com.group.groupemaker.service;

import com.group.groupemaker.dto.ListeDTO;
import com.group.groupemaker.model.Liste;
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

}
