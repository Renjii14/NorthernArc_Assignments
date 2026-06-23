package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.FineResponseDTO;

import java.util.List;

public interface FineService {

    FineResponseDTO getFineById(Long fineId);

    List<FineResponseDTO> getAllFines();

    FineResponseDTO payFine(Long fineId);
}
