package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.Player;
import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;
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
    public SeshStateWrapper joinAsController(String seshCode, String playerName, String socketId, string reconnectToken) {

        SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
        Player player = new Player(playerName, socketId);
        if (reconnectToken == null){
            state = joinSesh(seshInfo, player, "controller");
        }else{
            state = reJoinSesh(seshInfo, player, "controller", reconnectToken);
        }
    }

    @Override
    public SeshStateWrapper joinAsHost(String seshCode, String socketId, string reconnectToken) {
        
        SeshInfo seshInfo = checkSeshInfoPresent(getSeshInfo(seshCode));
        Player player = new Player("host", socketId);
        SeshStateWrapper state;
        if (reconnectToken == null){
            state = joinSesh(seshInfo, player, "host");
        }else{
            state = reJoinSesh(seshInfo, player, "host", reconnectToken);
        }
    }

    private SeshStateWrapper joinSesh(SeshInfo seshInfo, Player player, String role) {

        try {
            String apiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + role;
            ResponseEntity<SeshStateWrapper> responseEntity = restTemplate.postForEntity(apiUrl, player, SeshStateWrapper.class);
            return responseEntity.getBody();
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
        }
    }

        private SeshStateWrapper joinSesh(SeshInfo seshInfo, Player player, String role, String reconnectToken) {

        try {
            String apiUrl = backendBasePath + "/" + seshInfo.getSeshType() + "/seshs/" + seshInfo.getSeshCode() + "/players/" + role + "?reconnectToken=" + reconnectToken;
            ResponseEntity<SeshStateWrapper> responseEntity = restTemplate.postForEntity(apiUrl, player, SeshStateWrapper.class);
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

    private SeshInfo checkSeshInfoPresent(Optional<SeshInfo> seshInfoOptional){

        if (seshInfoOptional.isEmpty()){

            throw new RuntimeException();
        }
        return seshInfoOptional.get();
    }
}
