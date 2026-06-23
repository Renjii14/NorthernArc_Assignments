package org.northernarc.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookIssueRequestDTO {

    private Long bookId;
    private Long memberId;
    private Long librarianId;

    private LocalDate issueDate;
    private LocalDate dueDate;
}