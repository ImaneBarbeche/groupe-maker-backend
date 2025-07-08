package com.group.groupemaker.model;

public enum Profil {
    TIMIDE("Timide"),
    RESERVE("Réservé"),
    A_LAISE("À l'aise");

    private final String libelle;

    Profil(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
