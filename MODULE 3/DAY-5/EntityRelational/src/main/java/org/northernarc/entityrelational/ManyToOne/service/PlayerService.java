package org.northernarc.entityrelational.ManyToOne.service;

import org.northernarc.entityrelational.ManyToOne.entity.Player;

import java.util.List;

public interface PlayerService {

    Player addPlayer(Player player);
    List<Player> getAllPlayers();
    Player getPlayerById(Integer id);
    void deleteById(Integer id);
    Player updatePlayer(Integer id, Player player);
}
