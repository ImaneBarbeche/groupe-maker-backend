package com.group.groupemaker.repository;

import com.group.groupemaker.model.Groupe;
import com.group.groupemaker.model.Liste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    List<Groupe> findByListe(Liste liste);
}
