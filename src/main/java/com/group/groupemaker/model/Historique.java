package com.group.groupemaker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Historique {

    public Historique() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTirage;

    private String nomListe;

    @OneToMany
    private List<Groupe> groupes;

    @ManyToOne
    @JoinColumn(name = "liste_id", nullable = false)
    private Liste liste;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @PrePersist
    protected void onCreate() {
        this.dateTirage = LocalDateTime.now();
    }

    public List<Groupe> getGroupes() {
        return groupes;
    }

    public Liste getListe() {
        return liste;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTirage() {
        return dateTirage;
    }

    public String getNomListe() {
        return nomListe;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateTirage(LocalDateTime dateTirage) {
        this.dateTirage = dateTirage;
    }

    public void setGroupes(List<Groupe> groupes) {
        this.groupes = groupes;
    }

    public void setListe(Liste liste) {
        this.liste = liste;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setNomListe(String nomListe) {
        this.nomListe = nomListe;
    }
}
