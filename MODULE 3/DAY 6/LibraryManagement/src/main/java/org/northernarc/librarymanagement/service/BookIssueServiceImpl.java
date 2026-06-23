package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.BookIssueRequestDTO;
import org.northernarc.librarymanagement.dto.BookIssueResponseDTO;
import org.northernarc.librarymanagement.dto.BookIssueUpdateDTO;
import org.northernarc.librarymanagement.exception.BookIssueNotFound;
import org.northernarc.librarymanagement.exception.BookNotFound;
import org.northernarc.librarymanagement.exception.LibrarianNotFound;
import org.northernarc.librarymanagement.exception.MemberNotFound;
import org.northernarc.librarymanagement.model.Book;
import org.northernarc.librarymanagement.model.BookIssue;
import org.northernarc.librarymanagement.model.Librarian;
import org.northernarc.librarymanagement.model.Member;
import org.northernarc.librarymanagement.repository.BookIssueRepository;
import org.northernarc.librarymanagement.repository.BookRepository;
import org.northernarc.librarymanagement.repository.LibrarianRepository;
import org.northernarc.librarymanagement.repository.MemberRepository;
import org.northernarc.librarymanagement.service.BookIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookIssueServiceImpl implements BookIssueService {

    @Autowired
    private BookIssueRepository bookIssueRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    @Override
    public BookIssueResponseDTO issueBook(BookIssueRequestDTO dto) {

        BookIssue issue = mapToEntity(dto);

        Book book = issue.getBook();

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BookIssue saved = bookIssueRepository.save(issue);

        return mapToResponseDTO(saved);
    }

    @Override
    public BookIssueResponseDTO updateBookIssue(Long issueId,
                                                BookIssueUpdateDTO dto) {

        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() ->
                        new BookIssueNotFound(
                                "Book Issue not found with id : "
                                        + issueId));

        issue.setDueDate(dto.getDueDate());
        issue.setReturnDate(dto.getReturnDate());

        BookIssue updated = bookIssueRepository.save(issue);

        return mapToResponseDTO(updated);
    }

    @Override
    public BookIssueResponseDTO getBookIssueById(Long issueId) {

        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() ->
                        new BookIssueNotFound(
                                "Book Issue not found with id : "
                                        + issueId));

        return mapToResponseDTO(issue);
    }

    @Override
    public List<BookIssueResponseDTO> getAllBookIssues() {

        return bookIssueRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Override
    public void deleteBookIssue(Long issueId) {

        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() ->
                        new BookIssueNotFound(
                                "Book Issue not found with id : "
                                        + issueId));

        bookIssueRepository.delete(issue);
    }

    @Override
    public BookIssueResponseDTO returnBook(Long issueId) {

        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() ->
                        new BookIssueNotFound(
                                "Book Issue not found with id : "
                                        + issueId));

        issue.setReturnDate(LocalDate.now());

        Book book = issue.getBook();

        book.setAvailableCopies(
                book.getAvailableCopies() + 1);

        bookRepository.save(book);

        BookIssue updated =
                bookIssueRepository.save(issue);

        return mapToResponseDTO(updated);
    }

    private BookIssue mapToEntity(
            BookIssueRequestDTO dto) {

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() ->
                        new BookNotFound(
                                "Book not found with id : "
                                        + dto.getBookId()));

        Member member = memberRepository
                .findById(dto.getMemberId())
                .orElseThrow(() ->
                        new MemberNotFound(
                                "Member not found with id : "
                                        + dto.getMemberId()));

        Librarian librarian = librarianRepository
                .findById(dto.getLibrarianId())
                .orElseThrow(() ->
                        new LibrarianNotFound(
                                "Librarian not found with id : "
                                        + dto.getLibrarianId()));

        BookIssue issue = new BookIssue();

        issue.setIssueDate(dto.getIssueDate());
        issue.setDueDate(dto.getDueDate());

        issue.setBook(book);
        issue.setMember(member);
        issue.setLibrarian(librarian);

        return issue;
    }

    private BookIssueResponseDTO mapToResponseDTO(
            BookIssue issue) {

        return new BookIssueResponseDTO(
                issue.getIssueId(),
                issue.getBook().getBookId(),
                issue.getMember().getMemberId(),
                issue.getLibrarian().getLibrarianId(),
                issue.getIssueDate(),
                issue.getDueDate(),
                issue.getReturnDate()
        );
    }
}