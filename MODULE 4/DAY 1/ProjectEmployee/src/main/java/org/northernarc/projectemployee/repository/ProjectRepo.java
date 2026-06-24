package org.northernarc.projectemployee.repository;

import org.northernarc.projectemployee.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo  extends JpaRepository<Project,Integer> {
    Project findByName(String name);
}
