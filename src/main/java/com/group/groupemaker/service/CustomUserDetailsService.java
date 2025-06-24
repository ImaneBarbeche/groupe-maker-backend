package com.group.groupemaker.service;
import com.group.groupemaker.model.Utilisateur;
import com.group.groupemaker.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // 🔥 très important pour que Spring l’enregistre comme bean
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));

        // Adaptateur UserDetails
        return org.springframework.security.core.userdetails.User
            .withUsername(utilisateur.getEmail())
            .password(utilisateur.getMotDePasse()) // déjà hashé
            .roles(utilisateur.getRole()) // "USER", "FORMATEUR", etc.
            .build();
    }
}
