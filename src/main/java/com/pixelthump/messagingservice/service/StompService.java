package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;

public interface StompService {

    SeshStateWrapper joinAsController(String seshCode, String playerName, String reconnectToken);

    SeshStateWrapper joinAsHost(String seshCode, String reconnectToken);

    void sendCommandToSesh(Command message, String seshCode);
}
