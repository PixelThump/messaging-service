package com.pixelthump.messagingservice.rest.model;
import lombok.Data;

@Data
public class MessagingSeshUpdate {

    private MessagingSeshStateWrapper host;
    private MessagingSeshStateWrapper controller;
}
