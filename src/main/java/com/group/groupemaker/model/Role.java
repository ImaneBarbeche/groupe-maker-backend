package com.group.groupemaker.model;

public enum Role {
    USER("User"),
    ADMIN("Admin");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
