package com.group.groupemaker.repository;

import com.group.groupemaker.model.Personne;
import com.group.groupemaker.model.Liste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
    List<Personne> findByListe(Liste liste);
}
