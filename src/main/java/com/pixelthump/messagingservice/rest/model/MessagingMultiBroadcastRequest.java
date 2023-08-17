package com.pixelthump.messagingservice.rest.model;
import java.util.List;

public record MessagingMultiBroadcastRequest(List<String> recipients, Object payload) {

}
