package com.pixelthump.messagingservice.stomp.model;
import lombok.Data;

@Data
public class StompHeaders {

    private String reconnectToken;
    private String socketId;
    private String playerName;
}
