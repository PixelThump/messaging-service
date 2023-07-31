package com.pixelthump.messagingservice.stomp;

import com.pixelthump.messagingservice.stomp.model.message.StompMessage;

public interface StompMessageFactory {

    StompMessage getMessage(Object payload) throws UnsupportedOperationException;

    StompMessage getAckMessage();
}