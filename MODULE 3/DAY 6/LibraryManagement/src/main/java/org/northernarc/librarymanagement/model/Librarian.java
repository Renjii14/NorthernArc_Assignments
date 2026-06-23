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
public class Librarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "librarian_id")
    private Long librarianId;

    private String name;
    private String email;
    private String phoneNumber;
    @OneToMany(mappedBy = "librarian")
    private List<BookIssue> bookIssue;
}
