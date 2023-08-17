package com.pixelthump.messagingservice.service.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeshStateWrapper {

    private Object state;
    private String reconnectToken;

}
