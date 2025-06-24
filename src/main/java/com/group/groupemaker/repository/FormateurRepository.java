package com.group.groupemaker.repository;

import com.group.groupemaker.model.Formateur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FormateurRepository extends JpaRepository<Formateur, Long> {

    Optional<Formateur> findByUtilisateurEmail(String email);
}
