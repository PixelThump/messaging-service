package com.pixelthump.messagingservice.rest.model;
import java.util.Map;

public record MessagingDifferentBroadcastRequest(Map<String, Object> playerNameToPayload) {

}
