package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.service.model.SeshState;
import com.pixelthump.messagingservice.service.model.SeshUpdate;
import com.pixelthump.messagingservice.service.model.message.GenericStompMessage;
import com.pixelthump.messagingservice.service.model.message.StompMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class BroadcastServiceImplTest {

    @MockBean
    StompMessageFactory factory;
    @MockBean
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    BroadcastService messageBroadcaster;
    String sessionCode;


    @BeforeEach
    void setUp() {
        sessionCode = "abcd";
    }

    @Test
    void broadcastSeshUpdateToControllers_supportedPayload_shouldCallConvertAndSendWithCorrectDestination() {

        StompMessage stompMessage = new GenericStompMessage();
        when(factory.getMessage(any())).thenReturn(stompMessage);
        SeshUpdate seshUpdate = new SeshUpdate();
        messageBroadcaster.broadcastToSesh(sessionCode, seshUpdate);
        verify(messagingTemplate).convertAndSend("/topic/sesh/" + sessionCode + "/controller", stompMessage);
        verify(messagingTemplate).convertAndSend("/topic/sesh/" + sessionCode + "/host", stompMessage);
    }

    @Test
    void broadcastSeshUpdateToControllers_WITH_NON_SUPPORTED_PAYLOAD_SHOULD_THROW_EXCEPTION() {

        SeshUpdate seshUpdate = new SeshUpdate();
        seshUpdate.setController("hi");
        seshUpdate.setHost("hi");
        StompMessage stompMessage = new GenericStompMessage();
        when(factory.getMessage(any())).thenReturn(stompMessage).thenThrow(new UnsupportedOperationException());
        assertThrows(UnsupportedOperationException.class, () -> messageBroadcaster.broadcastToSesh(sessionCode, seshUpdate));
        verify(factory, times(2)).getMessage(any());
    }
    @Test
    void broadcastSeshUpdateToControllers_WITH_NON_SUPPORTED_PAYLOAD_SHOULD_THROW_EXCEPTION_FIRST() {

        SeshUpdate seshUpdate = new SeshUpdate();
        seshUpdate.setController("hi");
        seshUpdate.setHost("hi");
        StompMessage stompMessage = new GenericStompMessage();
        when(factory.getMessage(any())).thenThrow(new UnsupportedOperationException());
        assertThrows(UnsupportedOperationException.class, () -> messageBroadcaster.broadcastToSesh(sessionCode, seshUpdate));
        verify(factory, times(1)).getMessage(any());
    }
}