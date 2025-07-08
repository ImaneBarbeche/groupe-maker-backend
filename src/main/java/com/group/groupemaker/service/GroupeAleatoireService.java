package com.group.groupemaker.service;

import com.group.groupemaker.model.*;
import com.group.groupemaker.repository.HistoriqueRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupeAleatoireService {

    private final HistoriqueRepository historiqueRepository;

    public GroupeAleatoireService(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    public static class CriteresGeneration {
        private boolean mixerGenres = false;
        private boolean mixerAges = false;
        private boolean mixerNiveauxTechniques = false;
        private boolean mixerAisanceFrancais = false;
        private boolean mixerAnciensDWWM = false;
        private boolean mixerProfils = false;

        // Getters et setters
        public boolean isMixerGenres() { return mixerGenres; }
        public void setMixerGenres(boolean mixerGenres) { this.mixerGenres = mixerGenres; }
        
        public boolean isMixerAges() { return mixerAges; }
        public void setMixerAges(boolean mixerAges) { this.mixerAges = mixerAges; }
        
        public boolean isMixerNiveauxTechniques() { return mixerNiveauxTechniques; }
        public void setMixerNiveauxTechniques(boolean mixerNiveauxTechniques) { this.mixerNiveauxTechniques = mixerNiveauxTechniques; }
        
        public boolean isMixerAisanceFrancais() { return mixerAisanceFrancais; }
        public void setMixerAisanceFrancais(boolean mixerAisanceFrancais) { this.mixerAisanceFrancais = mixerAisanceFrancais; }
        
        public boolean isMixerAnciensDWWM() { return mixerAnciensDWWM; }
        public void setMixerAnciensDWWM(boolean mixerAnciensDWWM) { this.mixerAnciensDWWM = mixerAnciensDWWM; }
        
        public boolean isMixerProfils() { return mixerProfils; }
        public void setMixerProfils(boolean mixerProfils) { this.mixerProfils = mixerProfils; }
    }

    public List<List<Personne>> genererGroupesAleatoires(List<Personne> personnes, int nombreGroupes, 
                                                         CriteresGeneration criteres, Liste liste) {
        if (nombreGroupes <= 0 || personnes.isEmpty()) {
            throw new IllegalArgumentException("Nombre de groupes et liste de personnes doivent être valides");
        }

        // Obtenir l'historique des groupes précédents pour éviter les doublons
        List<Historique> historiquesPrecedents = historiqueRepository.findByListe(liste);
        Set<Set<Long>> combinaisonsExistantes = obtenirCombinaisonsExistantes(historiquesPrecedents);

        List<List<Personne>> meilleureTentative = null;
        int meilleureScore = -1;
        int maxTentatives = 1000;

        for (int tentative = 0; tentative < maxTentatives; tentative++) {
            List<List<Personne>> groupes = genererTentative(personnes, nombreGroupes, criteres);
            
            // Vérifier si cette combinaison existe déjà
            if (!combinaisonDejaUtilisee(groupes, combinaisonsExistantes)) {
                int score = evaluerQualiteGroupes(groupes, criteres);
                if (score > meilleureScore) {
                    meilleureScore = score;
                    meilleureTentative = new ArrayList<>(groupes);
                }
                
                // Si le score est parfait, on peut s'arrêter
                if (score >= 100) {
                    break;
                }
            }
        }

        return meilleureTentative != null ? meilleureTentative : genererTentativeSansHistorique(personnes, nombreGroupes);
    }

    private List<List<Personne>> genererTentative(List<Personne> personnes, int nombreGroupes, CriteresGeneration criteres) {
        List<Personne> personnesMelangees = new ArrayList<>(personnes);
        Collections.shuffle(personnesMelangees);

        List<List<Personne>> groupes = new ArrayList<>();
        for (int i = 0; i < nombreGroupes; i++) {
            groupes.add(new ArrayList<>());
        }

        // Distribution basique
        for (int i = 0; i < personnesMelangees.size(); i++) {
            groupes.get(i % nombreGroupes).add(personnesMelangees.get(i));
        }

        // Optimisation selon les critères
        if (criteres.isMixerGenres()) {
            optimiserDistributionGenres(groupes);
        }
        if (criteres.isMixerAnciensDWWM()) {
            optimiserDistributionAnciensDWWM(groupes);
        }

        return groupes;
    }

    private void optimiserDistributionGenres(List<List<Personne>> groupes) {
        // Redistribuer pour équilibrer les genres dans chaque groupe
        Map<Genre, List<Personne>> personnesParGenre = new HashMap<>();
        
        // Collecter toutes les personnes par genre
        for (List<Personne> groupe : groupes) {
            for (Personne personne : groupe) {
                personnesParGenre.computeIfAbsent(personne.getGenre(), k -> new ArrayList<>()).add(personne);
            }
        }

        // Vider tous les groupes
        groupes.forEach(List::clear);

        // Redistribuer équitablement
        int indexGroupe = 0;
        for (List<Personne> personnesDuGenre : personnesParGenre.values()) {
            for (Personne personne : personnesDuGenre) {
                groupes.get(indexGroupe % groupes.size()).add(personne);
                indexGroupe++;
            }
        }
    }

    private void optimiserDistributionAnciensDWWM(List<List<Personne>> groupes) {
        List<Personne> anciens = new ArrayList<>();
        List<Personne> nouveaux = new ArrayList<>();

        // Séparer anciens et nouveaux
        for (List<Personne> groupe : groupes) {
            for (Personne personne : groupe) {
                if (personne.getAncienDwwm()) {
                    anciens.add(personne);
                } else {
                    nouveaux.add(personne);
                }
            }
        }

        // Vider et redistribuer
        groupes.forEach(List::clear);

        // Distribuer les anciens d'abord
        for (int i = 0; i < anciens.size(); i++) {
            groupes.get(i % groupes.size()).add(anciens.get(i));
        }

        // Puis les nouveaux
        for (int i = 0; i < nouveaux.size(); i++) {
            groupes.get(i % groupes.size()).add(nouveaux.get(i));
        }
    }

    private int evaluerQualiteGroupes(List<List<Personne>> groupes, CriteresGeneration criteres) {
        int score = 0;
        int maxScore = 0;

        if (criteres.isMixerGenres()) {
            maxScore += 25;
            score += evaluerMixiteGenres(groupes);
        }

        if (criteres.isMixerAnciensDWWM()) {
            maxScore += 25;
            score += evaluerMixiteAnciensDWWM(groupes);
        }

        // Autres critères à implémenter...

        return maxScore > 0 ? (score * 100) / maxScore : 100;
    }

    private int evaluerMixiteGenres(List<List<Personne>> groupes) {
        int score = 0;
        for (List<Personne> groupe : groupes) {
            Set<Genre> genresPresents = groupe.stream()
                    .map(Personne::getGenre)
                    .collect(Collectors.toSet());
            if (genresPresents.size() > 1) {
                score += 25 / groupes.size();
            }
        }
        return score;
    }

    private int evaluerMixiteAnciensDWWM(List<List<Personne>> groupes) {
        int score = 0;
        for (List<Personne> groupe : groupes) {
            boolean aAncien = groupe.stream().anyMatch(Personne::getAncienDwwm);
            boolean aNouveau = groupe.stream().anyMatch(p -> !p.getAncienDwwm());
            if (aAncien && aNouveau) {
                score += 25 / groupes.size();
            }
        }
        return score;
    }

    private Set<Set<Long>> obtenirCombinaisonsExistantes(List<Historique> historiques) {
        Set<Set<Long>> combinaisons = new HashSet<>();
        
        for (Historique historique : historiques) {
            for (Groupe groupe : historique.getGroupes()) {
                Set<Long> combinaison = groupe.getPersonnes().stream()
                        .map(Personne::getId)
                        .collect(Collectors.toSet());
                if (combinaison.size() >= 2) { // Au moins 2 personnes pour former une combinaison
                    combinaisons.add(combinaison);
                }
            }
        }
        
        return combinaisons;
    }

    private boolean combinaisonDejaUtilisee(List<List<Personne>> groupes, Set<Set<Long>> combinaisonsExistantes) {
        for (List<Personne> groupe : groupes) {
            if (groupe.size() >= 2) {
                Set<Long> combinaison = groupe.stream()
                        .map(Personne::getId)
                        .collect(Collectors.toSet());
                if (combinaisonsExistantes.contains(combinaison)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<List<Personne>> genererTentativeSansHistorique(List<Personne> personnes, int nombreGroupes) {
        List<Personne> personnesMelangees = new ArrayList<>(personnes);
        Collections.shuffle(personnesMelangees);

        List<List<Personne>> groupes = new ArrayList<>();
        for (int i = 0; i < nombreGroupes; i++) {
            groupes.add(new ArrayList<>());
        }

        for (int i = 0; i < personnesMelangees.size(); i++) {
            groupes.get(i % nombreGroupes).add(personnesMelangees.get(i));
        }

        return groupes;
    }
}
