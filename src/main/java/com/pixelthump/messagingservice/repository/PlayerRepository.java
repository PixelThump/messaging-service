package com.pixelthump.messagingservice.repository;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    List<Player> deleteByPlayerId_SeshCode(String seshCode);


    List<Player> findByPlayerId_SeshCodeAndRole(String seshCode, Role role);


    Player findByPlayerId_SeshCodeAndPlayerId_PlayerName(String seshCode, String playerName);

    List<Player> findByPlayerId_SeshCode(String seshCode);

    boolean existsByPlayerId_SeshCodeAndPlayerId_PlayerNameAndReconnectToken(String seshCode, String playerName, String reconnectToken);



}