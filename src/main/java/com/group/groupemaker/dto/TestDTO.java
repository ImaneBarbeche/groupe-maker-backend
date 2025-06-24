package com.group.groupemaker.dto;

public class TestDTO {
    private Long idListe;
    private int nombreGroupes;

    public Long getIdListe() { return idListe; }
    public void setIdListe(Long idListe) { this.idListe = idListe; }

    public int getNombreGroupes() { return nombreGroupes; }
    public void setNombreGroupes(int nombreGroupes) { this.nombreGroupes = nombreGroupes; }

    @Override
    public String toString() {
        return "TestDTO{idListe=" + idListe + ", nombreGroupes=" + nombreGroupes + "}";
    }
}

