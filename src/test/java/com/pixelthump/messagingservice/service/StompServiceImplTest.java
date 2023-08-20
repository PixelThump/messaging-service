package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.PlayerId;
import com.pixelthump.messagingservice.repository.model.Role;
import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class StompServiceImplTest {

    @Autowired
    StompService stompService;
    @MockBean
    RestTemplate restTemplate;
    @MockBean
    PlayerRepository playerRepository;
    @Value("${pixelthump.backend-basepath}")
    private String backendBasePath;
    private String seshInfoUrl;
    private String seshCode;
    private SeshInfo seshInfo;

    @Test
    void joinAsController_shouldCallRestTemplateAndSavePlayer() {

        mockGetSeshInfo();

        ResponseEntity<Object> seshStateWrapperResponseEntity = new ResponseEntity<>("state", HttpStatusCode.valueOf(200));
        String quizxelApiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + Role.CONTROLLER.name().toLowerCase();
        when(restTemplate.postForEntity(eq(quizxelApiUrl), any(), eq(Object.class))).thenReturn(seshStateWrapperResponseEntity);

        String playerName = "playerName";
        Player player = new Player(Role.CONTROLLER, new PlayerId(seshCode, playerName), "reconnect", false);
        when(playerRepository.save(player)).thenReturn(player);

        SeshStateWrapper result = stompService.joinAsController(seshCode, playerName, null);


        SeshStateWrapper expected = new SeshStateWrapper("state", "reconnect");
        assertEquals(expected.getState(), result.getState());
        assertFalse(result.getReconnectToken().isEmpty());

        verify(restTemplate).getForEntity(seshInfoUrl, SeshInfo.class);
        verify(restTemplate).postForEntity(eq(quizxelApiUrl), any(), eq(Object.class));
        verify(playerRepository).save(any());
    }

    @Test
    void joinAsController_goodReJoin_shouldCallRestTemplateAndReturnState() {

        mockGetSeshInfo();

        String playerName = "playerName";
        Player player = new Player(Role.CONTROLLER, new PlayerId(seshCode, playerName), "reconnect", false);
        when(playerRepository.save(player)).thenReturn(player);

        when(playerRepository.findByPlayerId_SeshCodeAndPlayerId_PlayerName(any(), any())).thenReturn(player);


        ResponseEntity<Object> seshStateWrapperResponseEntity = new ResponseEntity<>("", HttpStatusCode.valueOf(200));
        String quizxelApiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + playerName + "/state";
        when(restTemplate.getForEntity(quizxelApiUrl, Object.class)).thenReturn(seshStateWrapperResponseEntity);

        SeshStateWrapper result = stompService.joinAsController(seshCode, playerName, "reconnect");
        SeshStateWrapper expected = new SeshStateWrapper("", "reconnect");

        assertEquals(expected.getState(), result.getState());
        assertFalse(result.getReconnectToken().isBlank());

        verify(restTemplate).getForEntity(seshInfoUrl, SeshInfo.class);
        verify(restTemplate).getForEntity(quizxelApiUrl, Object.class);
    }

    @Test
    void joinAsController_badReJoin_shouldCallRestTemplateAndReturnState() {

        mockGetSeshInfo();

        String playerName = "playerName";
        Player player = new Player(Role.CONTROLLER, new PlayerId(seshCode, playerName), "reconnect", false);
        when(playerRepository.save(player)).thenReturn(player);

        when(playerRepository.findByPlayerId_SeshCodeAndPlayerId_PlayerName(any(), any())).thenReturn(player);
        assertThrows(ResponseStatusException.class, () -> stompService.joinAsController(seshCode, playerName, "hackerMan"));

        verify(restTemplate).getForEntity(seshInfoUrl, SeshInfo.class);
    }

    @Test
    void joinAsHost() {

        mockGetSeshInfo();


        ResponseEntity<Object> seshStateWrapperResponseEntity = new ResponseEntity<>("state", HttpStatusCode.valueOf(200));
        String quizxelApiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + Role.HOST.name().toLowerCase();
        when(restTemplate.postForEntity(eq(quizxelApiUrl), any(), eq(Object.class))).thenReturn(seshStateWrapperResponseEntity);

        SeshStateWrapper result = stompService.joinAsHost(seshCode, null);

        SeshStateWrapper expected = new SeshStateWrapper("state", "reconnect");
        assertEquals(expected.getState(), result.getState());
        assertFalse(result.getReconnectToken().isEmpty());

        verify(restTemplate).getForEntity(seshInfoUrl, SeshInfo.class);
        verify(restTemplate).postForEntity(eq(quizxelApiUrl), any(), eq(Object.class));
        verify(playerRepository).save(any());
    }

    @Test
    void sendCommandToSesh() {

        mockGetSeshInfo();
        stompService.sendCommandToSesh(new Command(), seshCode);
        String commandUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/commands";
        verify(restTemplate).postForEntity(eq(commandUrl), any(), any());
    }

    private void mockGetSeshInfo() {

        seshCode = "seshCode";
        seshInfo = new SeshInfo("quizxel", seshCode);
        ResponseEntity<SeshInfo> seshInfoResponseEntity = new ResponseEntity<>(seshInfo, HttpStatusCode.valueOf(200));
        seshInfoUrl = backendBasePath + "/seshservice/seshs/" + seshCode;
        when(restTemplate.getForEntity(seshInfoUrl, SeshInfo.class)).thenReturn(seshInfoResponseEntity);
    }
}
