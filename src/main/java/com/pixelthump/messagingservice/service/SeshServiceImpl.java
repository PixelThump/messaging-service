package com.pixelthump.messagingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshState;
import com.pixelthump.messagingservice.stomp.model.message.CommandStompMessage;

@Component
public class SeshServiceImpl implements SeshService{

	private final RestTemplate restTemplate;
	@Value("${pixelthump.backend-basepath}")
	private String backendBasePath;

	@Autowired
	public SeshServiceImpl(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	private SeshInfo getSeshInfo(String seshCode) {

		try {
			String apiUrl = backendBasePath + "/seshservice/seshs/" + seshCode;
			ResponseEntity<SeshInfo> responseEntity = restTemplate.getForEntity(apiUrl,SeshInfo.class);
			return responseEntity.getBody();
		}catch (RestClientException e){
			throw new ResponseStatusException(HttpStatusCode.valueOf(404), e.getMessage());
		}
	}

	@Override
	public SeshState joinAsController(String seshCode, String playerName, String socketId) {

		SeshInfo seshInfo = getSeshInfo(seshCode);
		return null;
	}

	@Override
	public SeshState joinAsHost(String seshCode, String socketId) {
		return null;
	}

	@Override
	public void sendCommandToSesh(CommandStompMessage message, String seshCode) {


	}
}
