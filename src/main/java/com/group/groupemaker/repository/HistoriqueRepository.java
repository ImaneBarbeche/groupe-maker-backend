package com.group.groupemaker.repository;

import com.group.groupemaker.model.Historique;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueRepository extends JpaRepository<Historique, Long> {
    List<Historique> findByListe(Liste liste);
    List<Historique> findByUtilisateur(Utilisateur utilisateur);
}
