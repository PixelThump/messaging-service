package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StompMessageFactoryImpl implements StompMessageFactory {

    public StompMessage getMessage(Object payload) throws UnsupportedOperationException {

        final StompMessage message;

        if (payload instanceof Command messagingCommand) {

            message = getMessage(messagingCommand);

        } else if (payload instanceof RuntimeException exception) {

            message = getMessage(exception);

        } else if (payload instanceof SeshStateWrapper state) {

            message = getMessage(state);
        } else {

            String errorMessage = "Could not create StompMessage. Unsupported payload type";
            log.error(errorMessage);
            log.error("payload={}", payload);
            throw new UnsupportedOperationException(errorMessage);
        }

        return message;
    }

    private CommandStompMessage getMessage(Command messagingCommand) {

        final CommandStompMessage message = new CommandStompMessage();
        message.setCommand(messagingCommand);
        return message;
    }

    private StateStompMessage getMessage(SeshStateWrapper seshState) {

        final StateStompMessage message = new StateStompMessage();
        message.setState(seshState.getState());
        return message;
    }

    @Override
    public StompMessage getAckMessage() {

        return new GenericStompMessage();
    }

    @Override
    public StompMessage getGenericMessage(Object payload) {

        return new GenericStompMessage(payload);
    }

    private ErrorStompMessage getMessage(RuntimeException exception) {

        final ErrorStompMessage message = new ErrorStompMessage();
        message.setError(exception.getMessage());
        return message;
    }
}