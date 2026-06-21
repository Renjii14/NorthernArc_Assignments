package org.northernarc.entityrelational.ManyToOne.service;

import org.northernarc.entityrelational.ManyToOne.entity.Team;

import java.util.List;

public interface TeamService {

    Team addTeam(Team team);
    List<Team> getAllTeams();
    Team getTeamById(Integer id);
    void deleteTeamById(Integer id);
    Team updateTeamById(Integer id, Team team);
}
