package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.LibrarianRequestDTO;
import org.northernarc.librarymanagement.dto.LibrarianResponseDTO;
import org.northernarc.librarymanagement.dto.LibrarianUpdateDTO;
import org.northernarc.librarymanagement.exception.LibrarianNotFound;
import org.northernarc.librarymanagement.model.Librarian;
import org.northernarc.librarymanagement.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibrarianServiceImpl implements LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    public LibrarianResponseDTO addLibrarian(
            LibrarianRequestDTO librarianRequestDTO) {

        Librarian librarian =
                mapToEntity(librarianRequestDTO);

        Librarian saved =
                librarianRepository.save(librarian);

        return mapToResponseDTO(saved);
    }

    @Override
    public LibrarianResponseDTO updateLibrarian(
            Long librarianId,
            LibrarianUpdateDTO librarianUpdateDTO) {

        Librarian librarian =
                librarianRepository.findById(librarianId)
                        .orElseThrow(() ->
                                new LibrarianNotFound(
                                        "Librarian not found with id : "
                                                + librarianId));

        librarian.setName(
                librarianUpdateDTO.getName());

        librarian.setEmail(
                librarianUpdateDTO.getEmail());

        librarian.setPhoneNumber(
                librarianUpdateDTO.getPhoneNumber());

        Librarian updated =
                librarianRepository.save(librarian);

        return mapToResponseDTO(updated);
    }

    @Override
    public LibrarianResponseDTO getLibrarianById(
            Long librarianId) {

        Librarian librarian =
                librarianRepository.findById(librarianId)
                        .orElseThrow(() ->
                                new LibrarianNotFound(
                                        "Librarian not found with id : "
                                                + librarianId));

        return mapToResponseDTO(librarian);
    }

    @Override
    public List<LibrarianResponseDTO> getAllLibrarians() {

        return librarianRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public void deleteLibrarian(Long librarianId) {

        Librarian librarian =
                librarianRepository.findById(librarianId)
                        .orElseThrow(() ->
                                new LibrarianNotFound(
                                        "Librarian not found with id : "
                                                + librarianId));

        librarianRepository.delete(librarian);
    }

    private Librarian mapToEntity(
            LibrarianRequestDTO dto) {

        Librarian librarian = new Librarian();

        librarian.setName(dto.getName());
        librarian.setEmail(dto.getEmail());
        librarian.setPhoneNumber(
                dto.getPhoneNumber());

        return librarian;
    }

    private LibrarianResponseDTO mapToResponseDTO(
            Librarian librarian) {

        return new LibrarianResponseDTO(
                librarian.getLibrarianId(),
                librarian.getName()
        );
    }
}