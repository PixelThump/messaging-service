package com.pixelthump.messagingservice.stomp;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.service.StompMessageFactory;
import com.pixelthump.messagingservice.service.StompService;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.Command;
import com.pixelthump.messagingservice.service.model.message.CommandStompMessage;
import com.pixelthump.messagingservice.service.model.message.GenericStompMessage;
import com.pixelthump.messagingservice.service.model.message.StompMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class SeshControllerTest {

    @Autowired
    SeshController seshController;
    @MockBean
    StompService stompService;
    @MockBean
    StompMessageFactory messageFactory;
    @MockBean
    StompHeaderAccessor headerAccessor;
    String seshCode = "abcd";
    String playerName = "playerName";
    SeshStateWrapper state;
    String reconnectToken = "reconnect";

    @BeforeEach
    void setUp() {

        state = new SeshStateWrapper("state", null);
        when(messageFactory.getMessage(state)).thenReturn(new GenericStompMessage(state));
    }

    private void mockNullReconnectToken() {

        when(headerAccessor.getNativeHeader("reconnectToken")).thenReturn(null);
    }

    void mockNonNullReconnectToken() {

        when(headerAccessor.getNativeHeader("reconnectToken")).thenReturn(Collections.singletonList(reconnectToken));
    }

    @Test
    void joinSeshAsControllerWithPlayernameInTopic_nullReconnectToken_working() {
        mockNullReconnectToken();
        when(stompService.joinAsController(seshCode, playerName, null)).thenReturn(state);
        StompMessage result = seshController.joinSeshAsControllerWithPlayernameInTopic(seshCode, playerName, headerAccessor);
        StompMessage expected = new GenericStompMessage(state);
        assertEquals(expected, result);
    }

    @Test
    void joinSeshAsHost() {

        mockNullReconnectToken();
        when(stompService.joinAsHost(seshCode, null)).thenReturn(state);
        StompMessage result = seshController.joinSeshAsHost(seshCode, headerAccessor);
        StompMessage expected = new GenericStompMessage(state);
        assertEquals(expected, result);
    }

    @Test
    void sendCommandToSesh() {

        when(messageFactory.getAckMessage()).thenReturn(new GenericStompMessage());
        StompMessage result = seshController.sendCommandToSesh(new CommandStompMessage(new Command(playerName,"type","nithing")),seshCode,playerName);
        assertEquals(new GenericStompMessage(), result);
        verify(stompService).sendCommandToSesh(any(),eq(seshCode));

    }
}