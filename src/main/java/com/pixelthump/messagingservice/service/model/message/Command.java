package com.pixelthump.messagingservice.service.model.message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {

    private String playerId;
    private String type;
    private String body;
}
