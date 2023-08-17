package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.PlayerId;
import com.pixelthump.messagingservice.repository.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class BroadcastServiceImplTest {

    @Autowired
    BroadcastService broadcastService;
    @MockBean
    SimpMessagingTemplate messagingTemplate;
    @MockBean
    PlayerRepository playerRepository;
    @MockBean
    StompMessageFactory factory;
    private static final String SESH_BASE_PATH = "/topic/sesh/";
    private final String seshCode = "seshCode";

    @Test
    void broadcastToListOfPlayers() {

        List<Player> players = getPlayers();
        when(playerRepository.findByPlayerId_SeshCode(any())).thenReturn(players);

        List<String> recipients = new ArrayList<>();
        recipients.add(players.get(0).getPlayerId().getPlayerName());
        recipients.add(players.get(1).getPlayerId().getPlayerName());
        recipients.add(players.get(2).getPlayerId().getPlayerName());

        String payload = "payload";
        broadcastService.broadcastToListOfPlayers(seshCode, recipients, payload);

        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + players.get(0).getPlayerId().getSeshCode() + "/" + players.get(0).getPlayerId().getPlayerName(), payload);
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + players.get(1).getPlayerId().getSeshCode() + "/" + players.get(1).getPlayerId().getPlayerName(), payload);
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + players.get(2).getPlayerId().getSeshCode() + "/" + players.get(2).getPlayerId().getPlayerName(), payload);
    }

    @Test
    void broadcastToDifferentPlayers() {

        List<Player> players = getPlayers();

        when(playerRepository.findByPlayerId_SeshCode(seshCode)).thenReturn(players);
        Map<String, Object> recipients = new HashMap<>();
        recipients.put("abcd", "efgh");
        recipients.put("1234", 5678);
        broadcastService.broadcastToDifferentPlayers(seshCode, recipients);

        for (String recipientName : recipients.keySet()) {

            verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/" + recipientName, recipients.get(recipientName));
        }

        verify(messagingTemplate, never()).convertAndSend(SESH_BASE_PATH + seshCode + "/" + players.get(2).getPlayerId().getPlayerName(), recipients.get(players.get(2).getPlayerId().getPlayerName()));
    }

    private List<Player> getPlayers() {

        List<Player> players = new ArrayList<>();
        Player player1 = new Player(Role.CONTROLLER, new PlayerId(seshCode, "abcd"), "reconnectToo", false);
        Player player2 = new Player(Role.CONTROLLER, new PlayerId(seshCode, "1234"), "reconnect", false);
        Player player3 = new Player(Role.CONTROLLER, new PlayerId(seshCode, "XXXX"), "reconnectThree", false);
        players.add(player1);
        players.add(player2);
        players.add(player3);
        return players;
    }

    @Test
    void broadcastToSinglePlayer() {

        List<Player> players = getPlayers();

        Player player = players.get(0);
        String playerName = player.getPlayerId().getPlayerName();
        when(playerRepository.findByPlayerId_SeshCodeAndPlayerId_PlayerName(seshCode, playerName)).thenReturn(player);

        broadcastService.broadcastToSinglePlayer(seshCode, playerName, "payload");
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/" + playerName, "payload");
    }

    @Test
    void broadcastToAllPlayers() {

        List<Player> players = getPlayers();
        when(playerRepository.findByPlayerId_SeshCode(seshCode)).thenReturn(players);

        broadcastService.broadcastToAllPlayers(seshCode, "payload");

        for (Player player : players) {

            verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/" + player.getPlayerId().getPlayerName(), "payload");
        }
    }

    @Test
    void broadcastToRole() {

        List<Player> players = getPlayers();
        when(playerRepository.findByPlayerId_SeshCodeAndRole(seshCode, Role.CONTROLLER)).thenReturn(players);

        broadcastService.broadcastToRole(seshCode, Role.CONTROLLER, "payload");

        for (Player player : players) {

            verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/" + player.getPlayerId().getPlayerName(), "payload");
        }
    }
}