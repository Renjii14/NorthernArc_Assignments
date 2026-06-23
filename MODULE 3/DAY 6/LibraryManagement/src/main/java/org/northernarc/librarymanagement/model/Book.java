package org.northernarc.librarymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Long bookId;

    private String title;
    private String author;
    private String isbn;
    private int availableCopies;

    @OneToMany(mappedBy = "book")
    private List<BookIssue> bookIssue;
}
