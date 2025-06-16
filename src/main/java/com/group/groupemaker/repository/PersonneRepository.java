package com.group.groupemaker.repository;

import com.group.groupemaker.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
    
}

