package org.northernarc.librarymanagement.repository;

import org.northernarc.librarymanagement.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
