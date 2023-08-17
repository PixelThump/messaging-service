package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import com.pixelthump.messagingservice.repository.model.Player;
import com.pixelthump.messagingservice.repository.model.PlayerId;
import com.pixelthump.messagingservice.repository.model.Role;
import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
public class StompServiceImpl implements StompService {

    private final RestTemplate restTemplate;
    @Value("${pixelthump.backend-basepath}")
    private String backendBasePath;
    private final PlayerRepository playerRepository;

    @Autowired
    public StompServiceImpl(RestTemplate restTemplate, PlayerRepository playerRepository) {

        this.restTemplate = restTemplate;
        this.playerRepository = playerRepository;
    }

    private Optional<SeshInfo> getSeshInfo(String seshCode) {

        ResponseEntity<SeshInfo> responseEntity;
        try {
            String apiUrl = backendBasePath + "/seshservice/seshs/" + seshCode;
            responseEntity = restTemplate.getForEntity(apiUrl, SeshInfo.class);

        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }

        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public SeshStateWrapper joinAsController(String seshCode, String playerName, String reconnectToken) {

        if (playerName.equals("host")){

            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }

        SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
        SeshStateWrapper state;
        if (reconnectToken == null) {
            state = joinSesh(seshInfo, playerName, Role.CONTROLLER);
        } else {
            state = reJoinSesh(seshInfo, playerName, reconnectToken);
        }
        return state;
    }

    @Override
    public SeshStateWrapper joinAsHost(String seshCode, String reconnectToken) {

        SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
        SeshStateWrapper state;
        if (reconnectToken == null) {
            state = joinSesh(seshInfo, "host", Role.HOST);
        } else {
            state = reJoinSesh(seshInfo, "host", reconnectToken);
        }
        return state;
    }

    private SeshStateWrapper joinSesh(SeshInfo seshInfo, String playerName, Role role) {

        ResponseEntity<SeshStateWrapper> responseEntity;
        try {
            String apiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + role.name().toLowerCase();
            responseEntity = restTemplate.postForEntity(apiUrl, playerName, SeshStateWrapper.class);

        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
        PlayerId playerId = new PlayerId(seshInfo.getSeshCode(), playerName);
        Player player = new Player(role, playerId, generateReconnectToken(), false);
        playerRepository.save(player);
        return responseEntity.getBody();
    }

    private String generateReconnectToken() {
        return UUID.randomUUID().toString();
    }

    private SeshStateWrapper reJoinSesh(SeshInfo seshInfo, String playerName, String reconnectToken) {

        Player player = playerRepository.findByPlayerId_SeshCodeAndPlayerId_PlayerName(seshInfo.getSeshCode(), playerName);

        if (player == null){

            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }

        if (!player.getReconnectToken().equals(reconnectToken)){

            player.setReconnectFailed(true);
            playerRepository.save(player);
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }

        try {
            String apiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + playerName + "/state";
            ResponseEntity<SeshStateWrapper> responseEntity = restTemplate.getForEntity(apiUrl, SeshStateWrapper.class);
            player.setReconnectFailed(false);
            playerRepository.save(player);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
    }

    @Override
    public void sendCommandToSesh(Command message, String seshCode) {

        try {

            SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
            String apiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/commands";
            restTemplate.postForEntity(apiUrl, message, String.class);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
    }

    private SeshInfo checkSeshInfoPresent(Optional<SeshInfo> seshInfoOptional) {

        if (seshInfoOptional.isEmpty()) {

            throw new RuntimeException();
        }
        return seshInfoOptional.get();
    }
}
