package com.group.groupemaker.model;


import jakarta.persistence.*; // lie la classe à une table en base de données

@Entity // classe gérée comme une table
public class Liste {

    @Id // clé primaire de la table
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-incrémentation de l'id par postgreSQL
    private Long id; // type recommandé pour les identifiants JPA

    @Column(nullable = false)
    private String nom;

    @ManyToOne // lier une lsite à un utilisateur précis
    @JoinColumn(name = "utilisateur_id", nullable = false) // champ utilisateur doit être stocké dans la colonne
                                                           // utilisateur_id en base
    private Utilisateur utilisateur;

    public Liste() {
    }

    public Liste(String nom, Utilisateur utilisateur) {
        this.nom = nom;
        this.utilisateur = utilisateur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

}
