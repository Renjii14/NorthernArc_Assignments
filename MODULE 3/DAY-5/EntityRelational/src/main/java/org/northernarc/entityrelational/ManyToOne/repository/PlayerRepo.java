package org.northernarc.entityrelational.ManyToOne.repository;

import org.northernarc.entityrelational.ManyToOne.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepo extends JpaRepository<Player,Integer> {
}
