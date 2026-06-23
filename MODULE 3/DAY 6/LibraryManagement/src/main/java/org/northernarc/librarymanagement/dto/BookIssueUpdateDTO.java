package org.northernarc.librarymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookIssueUpdateDTO {

    private LocalDate dueDate;
    private LocalDate returnDate;
}