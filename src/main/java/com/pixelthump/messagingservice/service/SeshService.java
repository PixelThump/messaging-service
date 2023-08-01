package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;

public interface SeshService {

    SeshStateWrapper joinAsController(String seshCode, String playerName, String socketId);

    SeshStateWrapper joinAsHost(String seshCode, String socketId);

    void sendCommandToSesh(Command message, String seshCode);
}