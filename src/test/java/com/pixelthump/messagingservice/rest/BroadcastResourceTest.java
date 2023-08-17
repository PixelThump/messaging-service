package com.pixelthump.messagingservice.rest;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.PlayerId;
import com.pixelthump.messagingservice.repository.model.Role;
import com.pixelthump.messagingservice.rest.model.*;
import com.pixelthump.messagingservice.service.BroadcastService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@SpringBootTest(classes = Application.class)
class BroadcastResourceTest {

    @Autowired
    BroadcastResource broadcastResource;
    @MockBean
    BroadcastService broadcastService;

    private final String seshCode = "seshCode";

    @Test
    void broadcastToListOfPlayers() {
        List<Player> players = getPlayers();
        List<String> recipients = new ArrayList<>();
        recipients.add(players.get(0).getPlayerId().getPlayerName());
        recipients.add(players.get(1).getPlayerId().getPlayerName());
        recipients.add(players.get(2).getPlayerId().getPlayerName());
        broadcastResource.broadcastToListOfPlayers(seshCode, new MessagingMultiBroadcastRequest(recipients, "payload"));
        verify(broadcastService).broadcastToListOfPlayers(seshCode,recipients, "payload");
    }

    @Test
    void broadcastToDifferentPlayers() {

        Map<String, Object> recipients = new HashMap<>();
        recipients.put("abcd", "efgh");
        recipients.put("1234", 5678);

        broadcastResource.broadcastToDifferentPlayers(seshCode, new MessagingDifferentBroadcastRequest(recipients));

        verify(broadcastService).broadcastToDifferentPlayers(seshCode,recipients);
    }

    @Test
    void broadcastToSinglePlayer() {

        broadcastResource.broadcastToSinglePlayer(seshCode,new MessagingSinglePlayerBroadcastRequest("name","payload"));
        verify(broadcastService).broadcastToSinglePlayer(seshCode,"name", "payload");
    }

    @Test
    void broadcastToAllPlayers() {

        broadcastResource.broadcastToAllPlayers(seshCode,"payload");
        verify(broadcastService).broadcastToAllPlayers(seshCode, "payload");
    }

    @Test
    void broadcastToRole() {

        broadcastResource.broadcastToRole(seshCode,new MessagingRoleBroadcastRequest(MessagingRole.CONTROLLER, "payload"));
        verify(broadcastService).broadcastToRole(seshCode, Role.CONTROLLER, "payload");
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
}