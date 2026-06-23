package org.northernarc.librarymanagement.controller;

import org.northernarc.librarymanagement.dto.FineResponseDTO;
import org.northernarc.librarymanagement.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping("/{fineId}")
    public ResponseEntity<FineResponseDTO> getFineById(
            @PathVariable Long fineId) {

        return ResponseEntity.ok(
                fineService.getFineById(fineId));
    }

    @GetMapping
    public ResponseEntity<List<FineResponseDTO>> getAllFines() {

        return ResponseEntity.ok(
                fineService.getAllFines());
    }

    @PutMapping("/pay/{fineId}")
    public ResponseEntity<FineResponseDTO> payFine(
            @PathVariable Long fineId) {

        return ResponseEntity.ok(
                fineService.payFine(fineId));
    }
}