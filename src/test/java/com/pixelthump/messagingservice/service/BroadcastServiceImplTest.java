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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void broadcastToListOfPlayers() {

        List<Player> players = new ArrayList<>();
        Player player1 = new Player(Role.CONTROLLER, new PlayerId("abcd", "efgh"), "reconnectToo", false);
        Player player2 = new Player(Role.CONTROLLER, new PlayerId("abcd", "abcd"), "reconnect", false);
        players.add(player1);
        players.add(player2);
        when(playerRepository.findByPlayerId_SeshCode(any())).thenReturn(players);

        List<String> recipients = new ArrayList<>();
        recipients.add(player1.getPlayerId().getPlayerName());
        recipients.add(player2.getPlayerId().getPlayerName());

        String payload = "payload";
        broadcastService.broadcastToListOfPlayers("abcd", recipients, payload);

        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + player1.getPlayerId().getSeshCode() + "/" + player1.getPlayerId().getPlayerName(), payload);
        verify(messagingTemplate).convertAndSend(SESH_BASE_PATH + player2.getPlayerId().getSeshCode() + "/" + player2.getPlayerId().getPlayerName(), payload);
    }

    @Test
    void broadcastToDifferentPlayers() {

    }

    @Test
    void broadcastToSinglePlayer() {

    }

    @Test
    void broadcastToAllPlayers() {

    }

    @Test
    void broadcastToRole() {

    }
}