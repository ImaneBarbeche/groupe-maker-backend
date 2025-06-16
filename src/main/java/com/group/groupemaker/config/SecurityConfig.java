package com.group.groupemaker.config;

import com.group.groupemaker.service.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Fournit un encodeur pour hasher les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure la sécurité des routes
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF = protection contre les requêtes croisées
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/utilisateurs/register", "/utilisateurs/login").permitAll()
                        .anyRequest().authenticated()) // autorises l’accès libre à /register et /login, Toutes les
                                                       // autres routes (ex. /personnes, /listes) sont protégées
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin().disable() // désactives le formulaire HTML de login de Spring Boot
                .httpBasic().disable(); // désactives l’auth basique (popup navigateur)

        return http.build();
    }

}
