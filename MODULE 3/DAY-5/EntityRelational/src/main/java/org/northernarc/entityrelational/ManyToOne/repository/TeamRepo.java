package org.northernarc.entityrelational.ManyToOne.repository;

import org.northernarc.entityrelational.ManyToOne.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepo extends JpaRepository<Team,Integer> {
}
