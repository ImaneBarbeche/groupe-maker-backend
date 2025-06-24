package com.group.groupemaker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group.groupemaker.model.Liste;
import com.group.groupemaker.model.Utilisateur;

public interface ListeRepository extends JpaRepository<Liste, Long> {
    List<Liste> findByUtilisateur(Utilisateur utilisateur);

}
