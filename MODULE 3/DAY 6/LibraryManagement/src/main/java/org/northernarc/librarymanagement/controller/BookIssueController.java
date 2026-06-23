package org.northernarc.librarymanagement.controller;

import jakarta.validation.Valid;
import org.northernarc.librarymanagement.dto.BookIssueRequestDTO;
import org.northernarc.librarymanagement.dto.BookIssueResponseDTO;
import org.northernarc.librarymanagement.dto.BookIssueUpdateDTO;
import org.northernarc.librarymanagement.service.BookIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookissues")
public class BookIssueController {

    @Autowired
    private BookIssueService bookIssueService;

    @PostMapping
    public ResponseEntity<BookIssueResponseDTO> issueBook(
            @Valid @RequestBody BookIssueRequestDTO bookIssueRequestDTO) {

        return ResponseEntity.ok(
                bookIssueService.issueBook(bookIssueRequestDTO));
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<BookIssueResponseDTO> updateBookIssue(
            @PathVariable Long issueId,
            @Valid @RequestBody BookIssueUpdateDTO bookIssueUpdateDTO) {

        return ResponseEntity.ok(
                bookIssueService.updateBookIssue(
                        issueId,
                        bookIssueUpdateDTO));
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<BookIssueResponseDTO> getBookIssueById(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                bookIssueService.getBookIssueById(issueId));
    }

    @GetMapping
    public ResponseEntity<List<BookIssueResponseDTO>> getAllBookIssues() {

        return ResponseEntity.ok(
                bookIssueService.getAllBookIssues());
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<String> deleteBookIssue(
            @PathVariable Long issueId) {

        bookIssueService.deleteBookIssue(issueId);

        return ResponseEntity.ok(
                "Book Issue deleted successfully");
    }

    @PutMapping("/return/{issueId}")
    public ResponseEntity<BookIssueResponseDTO> returnBook(
            @PathVariable Long issueId) {

        return ResponseEntity.ok(
                bookIssueService.returnBook(issueId));
    }
}