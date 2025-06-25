package com.group.groupemaker.controller;

import com.group.groupemaker.model.Historique;
import com.group.groupemaker.repository.HistoriqueRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/historiques")
public class HistoriqueController {

    private final HistoriqueRepository historiqueRepository;

    public HistoriqueController(HistoriqueRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    @GetMapping
    public List<Historique> getAll() {
        return historiqueRepository.findAll();
    }

    @GetMapping("/{id}")
    public Historique getById(@PathVariable Long id) {
        return historiqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historique introuvable"));
    }

    @PostMapping
    public Historique create(@RequestBody Historique historique) {
        return historiqueRepository.save(historique);
    }

    @DeleteMapping("/{id}")
public void delete(@PathVariable Long id) {
    if (!historiqueRepository.existsById(id)) {
        throw new RuntimeException("Historique introuvable");
    }
    historiqueRepository.deleteById(id);
}

}
