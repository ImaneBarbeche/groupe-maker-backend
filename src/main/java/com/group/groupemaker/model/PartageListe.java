package com.group.groupemaker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PartageListe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "liste_id", nullable = false)
    private Liste liste;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private Utilisateur proprietaire;

    @ManyToOne
    @JoinColumn(name = "partage_avec_id", nullable = false)
    private Utilisateur partageAvec;

    @Column(nullable = false)
    private LocalDateTime datePartage;

    @Column(nullable = false)
    private boolean lectureSeule = true;

    public PartageListe() {}

    public PartageListe(Liste liste, Utilisateur proprietaire, Utilisateur partageAvec) {
        this.liste = liste;
        this.proprietaire = proprietaire;
        this.partageAvec = partageAvec;
        this.datePartage = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.datePartage = LocalDateTime.now();
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Liste getListe() {
        return liste;
    }

    public void setListe(Liste liste) {
        this.liste = liste;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Utilisateur getPartageAvec() {
        return partageAvec;
    }

    public void setPartageAvec(Utilisateur partageAvec) {
        this.partageAvec = partageAvec;
    }

    public LocalDateTime getDatePartage() {
        return datePartage;
    }

    public void setDatePartage(LocalDateTime datePartage) {
        this.datePartage = datePartage;
    }

    public boolean isLectureSeule() {
        return lectureSeule;
    }

    public void setLectureSeule(boolean lectureSeule) {
        this.lectureSeule = lectureSeule;
    }
}
