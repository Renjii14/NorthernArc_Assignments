package org.northernarc.librarymanagement.repository;

import org.northernarc.librarymanagement.model.BookIssue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookIssueRepository extends JpaRepository<BookIssue, Long> {
}
