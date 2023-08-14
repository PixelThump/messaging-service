package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;

public interface SeshService {

    SeshStateWrapper joinAsController(String seshCode, String playerName, String socketId, string reconnectToken);

    SeshStateWrapper joinAsHost(String seshCode, String socketId, string reconnectToken);

    void sendCommandToSesh(Command message, String seshCode);
}
