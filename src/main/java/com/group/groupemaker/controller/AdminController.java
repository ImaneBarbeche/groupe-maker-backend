package com.group.groupemaker.controller;

import com.group.groupemaker.model.Role;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import com.group.groupemaker.repository.ListeRepository;
import com.group.groupemaker.repository.HistoriqueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UtilisateurRepository utilisateurRepository;
    private final ListeRepository listeRepository;
    private final HistoriqueRepository historiqueRepository;

    public AdminController(UtilisateurRepository utilisateurRepository,
                          ListeRepository listeRepository,
                          HistoriqueRepository historiqueRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.listeRepository = listeRepository;
        this.historiqueRepository = historiqueRepository;
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!utilisateurRepository.existsById(id)) {
            response.put("success", false);
            response.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        utilisateurRepository.deleteById(id);
        response.put("success", true);
        response.put("message", "Utilisateur supprimé avec succès");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/utilisateurs/{id}/role")
    public ResponseEntity<Map<String, Object>> changeUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        try {
            Role newRole = Role.valueOf(request.get("role").toUpperCase());
            utilisateur.setRole(newRole);
            utilisateurRepository.save(utilisateur);

            response.put("success", true);
            response.put("message", "Rôle modifié avec succès");
            response.put("data", utilisateur);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Rôle invalide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiques() {
        Map<String, Object> stats = new HashMap<>();

        // Nombre total d'utilisateurs
        long totalUtilisateurs = utilisateurRepository.count();
        stats.put("totalUtilisateurs", totalUtilisateurs);

        // Nombre total de listes
        long totalListes = listeRepository.count();
        stats.put("totalListes", totalListes);

        // Nombre total de tirages (historiques)
        long totalTirages = historiqueRepository.count();
        stats.put("totalTirages", totalTirages);

        // Moyennes
        if (totalUtilisateurs > 0) {
            stats.put("moyenneListesParUtilisateur", (double) totalListes / totalUtilisateurs);
        } else {
            stats.put("moyenneListesParUtilisateur", 0.0);
        }

        if (totalListes > 0) {
            stats.put("moyenneTiragesParListe", (double) totalTirages / totalListes);
        } else {
            stats.put("moyenneTiragesParListe", 0.0);
        }

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/utilisateurs/statistiques")
    public ResponseEntity<List<Map<String, Object>>> getUserStatistics() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        List<Map<String, Object>> userStats = utilisateurs.stream()
                .map(user -> {
                    Map<String, Object> stats = new HashMap<>();
                    stats.put("id", user.getId());
                    stats.put("nom", user.getNom());
                    stats.put("prenom", user.getPrenom());
                    stats.put("dateCreation", user.getDateCreation());
                    stats.put("active", user.isActive());
                    stats.put("role", user.getRole());

                    // Nombre de listes créées par cet utilisateur
                    long nbListes = listeRepository.countByUtilisateur(user);
                    stats.put("nombreListes", nbListes);

                    // Nombre de tirages effectués par cet utilisateur
                    long nbTirages = historiqueRepository.findByUtilisateur(user).size();
                    stats.put("nombreTirages", nbTirages);

                    return stats;
                })
                .toList();

        return ResponseEntity.ok(userStats);
    }
}
