package com.group.groupemaker.repository;

import com.group.groupemaker.model.PartageListe;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.model.Liste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartagesListeRepository extends JpaRepository<PartageListe, Long> {
    
    List<PartageListe> findByPartageAvec(Utilisateur partageAvec);
    
    List<PartageListe> findByProprietaire(Utilisateur proprietaire);
    
    List<PartageListe> findByListe(Liste liste);
    
    Optional<PartageListe> findByListeAndPartageAvec(Liste liste, Utilisateur partageAvec);
    
    @Query("SELECT p FROM PartageListe p WHERE p.partageAvec = :utilisateur OR p.proprietaire = :utilisateur")
    List<PartageListe> findAllPartagesForUser(@Param("utilisateur") Utilisateur utilisateur);
}
