package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
class StompMessageFactoryImplTest {
    @Autowired
    StompMessageFactory messageFactory;
    String playerName;
    String errorMessage;

    @BeforeEach
    void setUp() {

        errorMessage = "This is an error!";
        playerName = "roboter5123";
    }

    @Test
    void getMessage_WITH_EXCEPTION_SHOULD_RETURN_ERROR_STOMP_MESSAGE_WITH_EXCEPTION_MESSAGE() {

        Exception exception = new RuntimeException(errorMessage);
        ErrorStompMessage expected = new ErrorStompMessage();
        expected.setError(exception.getMessage());
        StompMessage result = messageFactory.getMessage(exception);
        assertEquals(expected, result);
    }

    @Test
    void getMessage_WITH_COMMAND_SHOULD_RETURN_COMMAND_STOMP_MESSAGE_WITH_COMMAND() {

        Command serviceCommand= new Command(playerName,"asd", "asd");

        Command command = new Command();
        command.setType("asd");
        command.setBody("asd");
        command.setPlayerName(playerName);

        CommandStompMessage expected = new CommandStompMessage();
        expected.setCommand(serviceCommand);
        StompMessage result = messageFactory.getMessage(command);
        assertEquals(expected, result);
    }

    @Test
    void getMessage_WITH_GAME_STATE_SHOULD_RETURN_GAME_STATE_STOMP_MESSAGE_WITH_GAME_STATE() {

        SeshStateWrapper state = new SeshStateWrapper();
        state.setState("hallo");
        StateStompMessage expected = new StateStompMessage();
        expected.setState(state.getState());
        StompMessage result = messageFactory.getMessage(state);
        assertEquals(expected, result);
    }

    @Test
    void GET_ACK_MESSAGE_SHOULD_RETURN_ACK_MESSAGE(){

        StompMessage expected = new GenericStompMessage();
        StompMessage result = messageFactory.getAckMessage();
        assertEquals(expected, result);
    }

    @Test
    void getGenericMessage() {

        StompMessage expected = new GenericStompMessage("abcd");
        StompMessage result = messageFactory.getGenericMessage("abcd");
        assertEquals(expected, result);
    }
}