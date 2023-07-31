package com.pixelthump.messagingservice.stomp;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.service.SeshService;
import com.pixelthump.messagingservice.service.StompMessageFactory;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Application.class)
class SeshControllerTest {

    @MockBean
    SeshService seshServiceMock;
    @MockBean
    StompMessageFactory factoryMock;
    @Autowired
    SeshController seshStompcontroller;
    String seshCode;
    String playerName;
    String socketId;

    @BeforeEach
    void setUp() {

        seshCode = "abcd";
        playerName = "roboter5123";
        socketId = "asfd7465asd";
    }

    @Test
    void joinSessionAsHost_should_return_error_message_when_called_with_non_existent_session() {

        ResponseStatusException exception = new ResponseStatusException(HttpStatusCode.valueOf(404));
        when(seshServiceMock.joinAsHost(seshCode, socketId)).thenThrow(exception);

        ErrorStompMessage expected = new ErrorStompMessage(exception.getMessage());
        when(factoryMock.getMessage(exception)).thenReturn(expected);

        StompMessage result = seshStompcontroller.joinSeshAsHost(seshCode, socketId);

        assertEquals(expected, result);
    }

    @Test
    void joinSessionAsHost_should_return_state_message_when_called_with_existing_session() {

        SeshStateWrapper state = new SeshStateWrapper();
        when(seshServiceMock.joinAsHost(seshCode, socketId)).thenReturn(state);

        StompMessage expected = new StateStompMessage(state);
        when(factoryMock.getMessage(any())).thenReturn(expected);

        StompMessage result = seshStompcontroller.joinSeshAsHost(seshCode, socketId);

        assertEquals(expected, result);
    }

    @Test
    void joinSessionAsController_should_return_error_message_when_called_with_non_existent_session() {

        ResponseStatusException exception = new ResponseStatusException(HttpStatusCode.valueOf(404));
        when(seshServiceMock.joinAsController(seshCode, playerName, socketId)).thenThrow(exception);

        ErrorStompMessage expected = new ErrorStompMessage(exception.getMessage());
        when(factoryMock.getMessage(exception)).thenReturn(expected);

        StompMessage result = seshStompcontroller.joinSeshAsController(playerName, seshCode, socketId);

        assertEquals(expected, result);
    }

    @Test
    void joinSessionAsController_should_return_state_message_when_called_with_existing_session() {

        SeshStateWrapper state = new SeshStateWrapper();
        when(seshServiceMock.joinAsController(seshCode, playerName, socketId)).thenReturn(state);

        StompMessage expected = new StateStompMessage(state);
        when(factoryMock.getMessage(any())).thenReturn(expected);

        StompMessage result = seshStompcontroller.joinSeshAsController(playerName, seshCode, socketId);

        assertEquals(expected, result);
    }

    @Test
    void sendCommandToGame_Should_not_thow_exception_and_call_game_add_command() {

        StompMessage expected = new GenericStompMessage();

        when(factoryMock.getAckMessage()).thenReturn(expected);

        Command incomingCommand = new Command(socketId, new Action<>(playerName, "Chat message"));
        CommandStompMessage incomingMessage = new CommandStompMessage(incomingCommand);
        StompMessage result = seshStompcontroller.sendCommandToSesh(incomingMessage, seshCode, this.socketId);
        assertEquals(expected, result);
        verify(seshServiceMock).sendCommandToSesh(incomingMessage, seshCode);
    }

    @Test
    void sendCommandToGame_with_nonexistant_seshcode_Should_Return_ErrorMessage() {

        ResponseStatusException exception = new ResponseStatusException(HttpStatusCode.valueOf(404));
        doThrow(exception).when(seshServiceMock).sendCommandToSesh(any(), any());
        when(factoryMock.getMessage(exception)).thenReturn(new ErrorStompMessage(exception.getMessage()));

        StompMessage expected = new ErrorStompMessage(exception.getMessage());

        Command incomingCommand = new Command(socketId, new Action<>(playerName, "Chat message"));
        CommandStompMessage incomingMessage = new CommandStompMessage(incomingCommand);
        StompMessage result = seshStompcontroller.sendCommandToSesh(incomingMessage, seshCode, this.socketId);
        assertEquals(expected, result);
    }
}