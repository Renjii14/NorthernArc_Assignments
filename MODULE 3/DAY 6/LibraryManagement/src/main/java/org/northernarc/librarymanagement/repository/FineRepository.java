package org.northernarc.librarymanagement.repository;

import org.northernarc.librarymanagement.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {
}
