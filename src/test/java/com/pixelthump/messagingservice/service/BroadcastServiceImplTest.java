package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.PlayerId;
import com.pixelthump.messagingservice.repository.model.Role;
import com.pixelthump.messagingservice.service.model.message.GenericStompMessage;
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
        String payload = "payload";
        when(factory.getGenericMessage(any())).thenReturn(new GenericStompMessage(payload));

        List<String> recipients = new ArrayList<>();
        recipients.add(players.get(0).getPlayerId().getPlayerName());
        recipients.add(players.get(1).getPlayerId().getPlayerName());
        recipients.add(players.get(2).getPlayerId().getPlayerName());

        broadcastService.broadcastToListOfPlayers(seshCode, recipients, payload);

        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + players.get(0).getPlayerId().getSeshCode() + "/controller/" + players.get(0).getPlayerId().getPlayerName(), new GenericStompMessage(payload));
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + players.get(1).getPlayerId().getSeshCode() + "/controller/" + players.get(1).getPlayerId().getPlayerName(), new GenericStompMessage(payload));
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + players.get(2).getPlayerId().getSeshCode() + "/controller/" + players.get(2).getPlayerId().getPlayerName(), new GenericStompMessage(payload));
    }

    @Test
    void broadcastToDifferentPlayers() {

        List<Player> players = getPlayers();

        when(playerRepository.findByPlayerId_SeshCode(seshCode)).thenReturn(players);
        Map<String, Object> recipients = new HashMap<>();
        recipients.put("abcd", "efgh");
        recipients.put("1234", 5678);
        when(factory.getGenericMessage("efgh")).thenReturn(new GenericStompMessage("efgh"));
        when(factory.getGenericMessage(5678)).thenReturn(new GenericStompMessage(5678));
        broadcastService.broadcastToDifferentPlayers(seshCode, recipients);

        for (String recipientName : recipients.keySet()) {

            verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/controller/" + recipientName, new GenericStompMessage(recipients.get(recipientName)));
        }

        verify(messagingTemplate, never()).convertAndSend(SESH_BASE_PATH + seshCode + "/controller/" + players.get(2).getPlayerId().getPlayerName(), new GenericStompMessage(recipients.get(players.get(2).getPlayerId().getPlayerName())));
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
        when(factory.getGenericMessage("payload")).thenReturn(new GenericStompMessage("payload"));

        broadcastService.broadcastToSinglePlayer(seshCode, playerName, "payload");
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/controller/" + playerName, new GenericStompMessage("payload"));
    }

    @Test
    void broadcastToAllPlayers() {

        List<Player> players = getPlayers();
        when(playerRepository.findByPlayerId_SeshCode(seshCode)).thenReturn(players);
        when(factory.getGenericMessage("payload")).thenReturn(new GenericStompMessage("payload"));

        broadcastService.broadcastToAllPlayers(seshCode, "payload");

        for (Player player : players) {

            verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/controller/" + player.getPlayerId().getPlayerName(), new GenericStompMessage("payload"));
        }
    }

    @Test
    void broadcastToRole() {

        List<Player> players = getPlayers();
        when(playerRepository.findByPlayerId_SeshCodeAndRole(seshCode, Role.CONTROLLER)).thenReturn(players);
        when(factory.getGenericMessage("payload")).thenReturn(new GenericStompMessage("payload"));

        broadcastService.broadcastToRole(seshCode, Role.CONTROLLER, "payload");

        for (Player player : players) {

            verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + seshCode + "/controller/" + player.getPlayerId().getPlayerName(), new GenericStompMessage("payload"));
        }
    }
}