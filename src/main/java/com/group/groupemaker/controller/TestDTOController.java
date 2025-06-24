package com.group.groupemaker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group.groupemaker.dto.TestDTO;

@RestController
@RequestMapping("/testdto")
public class TestDTOController {

    @PostMapping
    public ResponseEntity<String> test(@RequestBody TestDTO dto) {
        System.out.println("Reçu DTO: " + dto);
        return ResponseEntity.ok("DTO reçu avec idListe = " + dto.getIdListe());
    }
}