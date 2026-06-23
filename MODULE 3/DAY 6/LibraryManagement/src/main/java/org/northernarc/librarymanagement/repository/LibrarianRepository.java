package org.northernarc.librarymanagement.repository;

import org.northernarc.librarymanagement.model.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
}
