package com.pixelthump.messagingservice.stomp;
import com.pixelthump.messagingservice.service.StompMessageFactory;
import com.pixelthump.messagingservice.service.StompService;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.message.CommandStompMessage;
import com.pixelthump.messagingservice.service.model.message.StompMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
@Log4j2
public class SeshController {

    private final StompService stompService;
    private final StompMessageFactory messageFactory;

    @Autowired
    public SeshController(StompService stompService, StompMessageFactory messageFactory) {

        this.stompService = stompService;
        this.messageFactory = messageFactory;
    }

    @SubscribeMapping("/topic/sesh/{seshCode}/host")
    public StompMessage joinSeshAsHost(@DestinationVariable final String seshCode, final StompHeaderAccessor headerAccessor) {

        try {
            String reconnectToken = getReconnectToken(headerAccessor);
            log.info("StompControllerImpl: Entering joinSeshAsHost(seshCode={}, reconnectToken={}", seshCode, reconnectToken);
            SeshStateWrapper state = stompService.joinAsHost(seshCode, reconnectToken);
            StompMessage reply = messageFactory.getMessage(state);
            log.info("StompControllerImpl: Exiting joinSeshAsHost(reconectToken={}, reply={})", reconnectToken, reply);

            return reply;
        } catch (Exception e) {

            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsHost(reply={})", reply);
            return reply;
        }
    }

    @SubscribeMapping("/topic/sesh/{seshCode}/controller/{playerName}")
    public StompMessage joinSeshAsControllerWithPlayernameInTopic(@DestinationVariable final String seshCode, @DestinationVariable final String playerName, final StompHeaderAccessor headerAccessor) {

        try {
            String reconnectToken = getReconnectToken(headerAccessor);
            log.info("Started joinSeshAsControllerWithPlayernameInTopic with playerName={} seshCode={}, reconnectToken={}", playerName, seshCode, reconnectToken);

            SeshStateWrapper state;
            if (Objects.equals(playerName, "host")) {
                state = stompService.joinAsHost(seshCode, reconnectToken);
            } else {
                state = stompService.joinAsController(seshCode, playerName, reconnectToken);
            }

            StompMessage reply = messageFactory.getMessage(state);
            log.info("Finished joinSeshAsControllerWithPlayernameInTopic with playerName={}, seshCode={}, reconnectToken={}, reply={}", playerName, seshCode, reconnectToken, reply);
            return reply;
        } catch (Exception e) {
            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsControllerWithPlayernameInTopic(reply={})", reply);
            return reply;
        }
    }

    private String getReconnectToken(StompHeaderAccessor headerAccessor) {

        List<String> reconnectTokenList = headerAccessor.getNativeHeader("reconnectToken");
        if (reconnectTokenList == null || reconnectTokenList.isEmpty()) {
            return null;
        } else {
            return reconnectTokenList.get(0);
        }
    }

    @MessageMapping("/topic/sesh/{seshCode}")
    public StompMessage sendCommandToSesh(final CommandStompMessage message, @DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId) {

        log.info("Entering sendCommandToSesh with message={}, seshCode={}, socketId={}", message, seshCode, socketId);
        try {
            this.stompService.sendCommandToSesh(message.getCommand(), seshCode);
            StompMessage reply = messageFactory.getAckMessage();
            log.info("Exiting sendCommandToSesh with reply={}", reply);

            return reply;
        } catch (Exception e) {

            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsHost(reply={})", reply);
            return reply;
        }
    }
}
