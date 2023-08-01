package com.pixelthump.messagingservice.rest.model;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import lombok.Data;

@Data
public class MessagingSeshUpdate {

    private SeshStateWrapper host;
    private SeshStateWrapper controller;
}
