package com.group.groupemaker.model;

public enum Genre {
    MASCULIN("Masculin"),
    FEMININ("Féminin"),
    NE_SE_PRONONCE_PAS("Ne se prononce pas");

    private final String libelle;

    Genre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
