package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.service.model.SeshUpdate;
import com.pixelthump.messagingservice.service.model.message.StompMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BroadcastServiceImpl implements BroadcastService {

    private final SimpMessagingTemplate messagingTemplate;
    private static final String SESH_BASE_PATH = "/topic/sesh/";
    private static final String ERROR_MESSAGE_LINE_2 = "No message type available in message factory for type {}";
    private static final String ERROR_MESSAGE_LINE_1 = "Could not broadcast message with payload {}";
    private final StompMessageFactory factory;

    @Autowired
    public BroadcastServiceImpl(SimpMessagingTemplate messagingTemplate, StompMessageFactory factory) {

        this.messagingTemplate = messagingTemplate;
        this.factory = factory;
    }

    @Override
    public void broadcastToSesh(String seshCode, SeshUpdate seshUpdate) {

        broadcastSeshUpdateToControllers(seshCode, seshUpdate.getController());
        broadcastSeshUpdateToHost(seshCode, seshUpdate.getHost());
    }

    private void broadcastSeshUpdateToControllers(String seshCode, Object payload) {

        final String destination = SESH_BASE_PATH + seshCode + "/controller";

        try {

            final StompMessage message = factory.getMessage(payload);
            broadcast(destination, message);

        } catch (UnsupportedOperationException e) {

            log.error(ERROR_MESSAGE_LINE_1, payload);
            log.error(ERROR_MESSAGE_LINE_2, payload.getClass());

            throw e;
        }
    }

    private void broadcastSeshUpdateToHost(String seshcode, Object payload) {

        final String destination = SESH_BASE_PATH + seshcode + "/host";

        try {

            final StompMessage message = factory.getMessage(payload);
            broadcast(destination, message);

        } catch (UnsupportedOperationException e) {

            log.error(ERROR_MESSAGE_LINE_1, payload);
            log.error(ERROR_MESSAGE_LINE_2, payload.getClass());

            throw e;
        }
    }

    private void broadcast(final String destination, final StompMessage message) {

        this.messagingTemplate.convertAndSend(destination, message);
    }
}
