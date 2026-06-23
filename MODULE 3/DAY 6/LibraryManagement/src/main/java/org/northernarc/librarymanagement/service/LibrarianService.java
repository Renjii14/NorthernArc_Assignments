package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.LibrarianRequestDTO;
import org.northernarc.librarymanagement.dto.LibrarianResponseDTO;
import org.northernarc.librarymanagement.dto.LibrarianUpdateDTO;

import java.util.List;

public interface LibrarianService {

    LibrarianResponseDTO addLibrarian(LibrarianRequestDTO librarianRequestDTO);

    LibrarianResponseDTO updateLibrarian(Long librarianId,LibrarianUpdateDTO librarianUpdateDTO);

    LibrarianResponseDTO getLibrarianById(Long librarianId);

    List<LibrarianResponseDTO> getAllLibrarians();

    void deleteLibrarian(Long librarianId);
}