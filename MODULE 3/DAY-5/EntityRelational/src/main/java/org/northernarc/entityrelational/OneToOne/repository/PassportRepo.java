package org.northernarc.entityrelational.OneToOne.repository;

import org.northernarc.entityrelational.OneToOne.model.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepo extends JpaRepository<Passport,Long> {
}
