package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.FineResponseDTO;
import org.northernarc.librarymanagement.exception.FineNotFound;
import org.northernarc.librarymanagement.model.Fine;
import org.northernarc.librarymanagement.repository.FineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FineServiceImpl implements FineService {

    @Autowired
    private FineRepository fineRepository;

    @Override
    public FineResponseDTO getFineById(Long fineId) {

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() ->
                        new FineNotFound(
                                "Fine not found with id : "
                                        + fineId));

        return mapToResponseDTO(fine);
    }

    @Override
    public List<FineResponseDTO> getAllFines() {

        return fineRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public FineResponseDTO payFine(Long fineId) {

        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() ->
                        new FineNotFound(
                                "Fine not found with id : "
                                        + fineId));

        fine.setPaymentStatus("PAID");

        Fine updated = fineRepository.save(fine);

        return mapToResponseDTO(updated);
    }

    private FineResponseDTO mapToResponseDTO(Fine fine) {

        return new FineResponseDTO(
                fine.getAmount(),
                fine.getPaymentStatus()
        );
    }
}