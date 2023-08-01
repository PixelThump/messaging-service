package com.pixelthump.messagingservice.service.model;
import lombok.Data;

@Data
public class SeshUpdate {

    private SeshStateWrapper host;
    private SeshStateWrapper controller;
}
