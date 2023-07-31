package com.pixelthump.messagingservice.service;

import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshState;
import com.pixelthump.messagingservice.stomp.model.message.CommandStompMessage;

public interface SeshService {

	SeshInfo getSeshInfo(String seshCode);

	SeshInfo hostSesh(String seshCode);

	SeshState joinAsController(String seshCode, String playerName, String socketId);

	SeshState joinAsHost(String seshCode, String socketId);

	void sendCommandToSesh(CommandStompMessage message, String seshCode);
}