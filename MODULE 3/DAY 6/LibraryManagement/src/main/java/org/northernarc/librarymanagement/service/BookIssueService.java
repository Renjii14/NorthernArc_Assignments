package org.northernarc.librarymanagement.service;

import org.northernarc.librarymanagement.dto.BookIssueRequestDTO;
import org.northernarc.librarymanagement.dto.BookIssueResponseDTO;
import org.northernarc.librarymanagement.dto.BookIssueUpdateDTO;

import java.util.List;

public interface BookIssueService {

    BookIssueResponseDTO issueBook(BookIssueRequestDTO bookIssueRequestDTO);

    BookIssueResponseDTO updateBookIssue(Long issueId,BookIssueUpdateDTO bookIssueUpdateDTO);

    BookIssueResponseDTO getBookIssueById(Long issueId);

    List<BookIssueResponseDTO> getAllBookIssues();

    void deleteBookIssue(Long issueId);

    BookIssueResponseDTO returnBook(Long issueId);
}
