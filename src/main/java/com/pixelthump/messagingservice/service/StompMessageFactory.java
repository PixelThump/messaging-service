package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.message.StompMessage;

public interface StompMessageFactory {

    StompMessage getMessage(Object payload) throws UnsupportedOperationException;

    StompMessage getAckMessage();

    StompMessage getGenericMessage(Object payload);
}