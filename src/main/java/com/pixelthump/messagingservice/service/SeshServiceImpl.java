package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.Player;
import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshState;
import com.pixelthump.messagingservice.service.model.message.CommandStompMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class SeshServiceImpl implements SeshService {

    private final RestTemplate restTemplate;
    @Value("${pixelthump.backend-basepath}")
    private String backendBasePath;

    @Autowired
    public SeshServiceImpl(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
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
    public SeshState joinAsController(String seshCode, String playerName, String socketId) {

        SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
        Player player = new Player(playerName, socketId);
        return joinSesh(seshCode, seshInfo.getName(), player, "controller");
    }

    @Override
    public SeshState joinAsHost(String seshCode, String socketId) {

        SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
        Player player = new Player("host", socketId);
        return joinSesh(seshCode, seshInfo.getName(), player, "host");
    }

    private SeshState joinSesh(String seshCode, String seshType, Player player, String role) {

        try {
            String apiUrl = backendBasePath + "/" + seshType + "/seshs/" + seshCode + "/players/" + role;
            ResponseEntity<SeshState> responseEntity = restTemplate.postForEntity(apiUrl, player, SeshState.class);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
    }

    @Override
    public void sendCommandToSesh(CommandStompMessage message, String seshCode) {

        try {

            SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
            String apiUrl = backendBasePath + "/" + seshInfo.getName() + "/seshs/" + seshCode + "/commands";
            restTemplate.postForEntity(apiUrl, message, String.class);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
    }

    private SeshInfo checkSeshInfoPresent(Optional<SeshInfo> seshInfoOptional){

        if (seshInfoOptional.isEmpty()){

            throw new RuntimeException();
        }
        return seshInfoOptional.get();
    }
}
