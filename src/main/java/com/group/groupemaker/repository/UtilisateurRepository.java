package com.group.groupemaker.repository;

import com.group.groupemaker.model.Utilisateur; // import de la classe utilisateur

import org.springframework.data.jpa.repository.JpaRepository; // import pour avoir accès aux méthodes save(), findaAll(), etc.

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> { // spring génère une classe avec les méthodes mentionnées dans l'import
}
