package org.northernarc.librarymanagement.controller;

import jakarta.validation.Valid;
import org.northernarc.librarymanagement.dto.LibrarianRequestDTO;
import org.northernarc.librarymanagement.dto.LibrarianResponseDTO;
import org.northernarc.librarymanagement.dto.LibrarianUpdateDTO;
import org.northernarc.librarymanagement.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/librarians")
public class LibrarianController {

    @Autowired
    private LibrarianService librarianService;

    @PostMapping
    public ResponseEntity<LibrarianResponseDTO> addLibrarian(
            @Valid @RequestBody LibrarianRequestDTO librarianRequestDTO) {

        return ResponseEntity.ok(
                librarianService.addLibrarian(librarianRequestDTO));
    }

    @PutMapping("/{librarianId}")
    public ResponseEntity<LibrarianResponseDTO> updateLibrarian(
            @PathVariable Long librarianId,
            @Valid @RequestBody LibrarianUpdateDTO librarianUpdateDTO) {

        return ResponseEntity.ok(
                librarianService.updateLibrarian(
                        librarianId,
                        librarianUpdateDTO));
    }

    @GetMapping("/{librarianId}")
    public ResponseEntity<LibrarianResponseDTO> getLibrarianById(
            @PathVariable Long librarianId) {

        return ResponseEntity.ok(
                librarianService.getLibrarianById(librarianId));
    }

    @GetMapping
    public ResponseEntity<List<LibrarianResponseDTO>> getAllLibrarians() {

        return ResponseEntity.ok(
                librarianService.getAllLibrarians());
    }

    @DeleteMapping("/{librarianId}")
    public ResponseEntity<String> deleteLibrarian(
            @PathVariable Long librarianId) {

        librarianService.deleteLibrarian(librarianId);

        return ResponseEntity.ok(
                "Librarian deleted successfully");
    }
}