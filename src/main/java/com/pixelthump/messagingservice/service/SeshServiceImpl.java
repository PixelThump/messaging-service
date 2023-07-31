package com.pixelthump.messagingservice.service;

import org.springframework.stereotype.Component;

import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshState;
import com.pixelthump.messagingservice.stomp.model.message.CommandStompMessage;

@Component
public class SeshServiceImpl implements SeshService{

	@Override
	public SeshInfo getSeshInfo(String seshCode) {
		return null;
	}

	@Override
	public SeshInfo hostSesh(String seshCode) {
		return null;
	}

	@Override
	public SeshState joinAsController(String seshCode, String playerName, String socketId) {
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
