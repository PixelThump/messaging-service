package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.Role;
import com.pixelthump.messagingservice.service.model.message.StompMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class BroadcastServiceImpl implements BroadcastService {

    private final SimpMessagingTemplate messagingTemplate;
    private static final String SESH_BASE_PATH = "/topic/sesh/";

    private final StompMessageFactory stompMessageFactory;
    private final PlayerRepository playerRepository;

    @Autowired
    public BroadcastServiceImpl(SimpMessagingTemplate messagingTemplate, StompMessageFactory stompMessageFactory, PlayerRepository playerRepository) {

        this.messagingTemplate = messagingTemplate;
        this.stompMessageFactory = stompMessageFactory;
        this.playerRepository = playerRepository;
    }

    @Override
    public void broadcastToListOfPlayers(String seshCode, List<String> recipients, Object payload) {

        List<Player> players = playerRepository.findByPlayerId_SeshCode(seshCode);
        players.parallelStream().filter(player -> recipients.contains(player.getPlayerId().getPlayerName())).forEach(player -> broadcastToPlayer(player, payload));
    }

    @Override
    public void broadcastToDifferentPlayers(String seshCode, Map<String, Object> playerNameToPayload) {

        List<Player> players = playerRepository.findByPlayerId_SeshCode(seshCode);
        players.parallelStream().filter(player -> playerNameToPayload.containsKey(player.getPlayerId().getPlayerName())).forEach(player -> broadcastToPlayer(player, playerNameToPayload.get(player.getPlayerId().getPlayerName())));
    }

    @Override
    public void broadcastToSinglePlayer(String seshCode, String playerName, Object payload) {

        Player player = playerRepository.findByPlayerId_SeshCodeAndPlayerId_PlayerName(seshCode, playerName);
        broadcastToPlayer(player, payload);
    }

    @Override
    public void broadcastToAllPlayers(String seshCode, Object payload) {

        List<Player> players = playerRepository.findByPlayerId_SeshCode(seshCode);
        players.parallelStream().forEach(player -> broadcastToPlayer(player, payload));
    }

    @Override
    public void broadcastToRole(String seshCode, Role role, Object payload) {

        List<Player> players = playerRepository.findByPlayerId_SeshCodeAndRole(seshCode, role);
        players.parallelStream().forEach(player -> broadcastToPlayer(player, payload));
    }

    private void broadcastToPlayer(Player player, Object payload) {


        String destination = SESH_BASE_PATH + player.getPlayerId().getSeshCode() + "/controller/" + player.getPlayerId().getPlayerName();
        StompMessage message = stompMessageFactory.getGenericMessage(payload);
        log.info("broadcastToPlayer: broacasting to destination={} message={}", destination, message);
        messagingTemplate.convertAndSend(destination, message);
    }
}
