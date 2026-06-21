package org.northernarc.jpa.controller;

import org.northernarc.jpa.model.FixedDeposit;
import org.northernarc.jpa.service.FixedDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/fixeddeposits")
public class FixedDepositController {

    @Autowired
    private FixedDepositService fixedDepositService;

    @GetMapping
    public Collection<FixedDeposit> listAll() {
        return fixedDepositService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FixedDeposit> findById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                fixedDepositService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<FixedDeposit> save(
            @RequestBody FixedDeposit fixedDeposit) {

        return ResponseEntity.status(201)
                .body(fixedDepositService.save(fixedDeposit));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FixedDeposit> update(
            @PathVariable int id,
            @RequestBody FixedDeposit fixedDeposit) {

        return ResponseEntity.ok(
                fixedDepositService.update(id, fixedDeposit)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable int id) {

        fixedDepositService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}