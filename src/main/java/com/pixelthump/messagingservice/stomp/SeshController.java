package com.pixelthump.messagingservice.stomp;
import com.pixelthump.messagingservice.rest.model.MessagingSeshUpdate;
import com.pixelthump.messagingservice.service.BroadcastService;
import com.pixelthump.messagingservice.service.SeshService;
import com.pixelthump.messagingservice.service.StompMessageFactory;
import com.pixelthump.messagingservice.service.model.SeshStateWrapper;
import com.pixelthump.messagingservice.service.model.SeshUpdate;
import com.pixelthump.messagingservice.service.model.message.CommandStompMessage;
import com.pixelthump.messagingservice.service.model.message.StompMessage;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class SeshController {

    private final SeshService seshService;
    private final StompMessageFactory messageFactory;
    private final ModelMapper modelMapper;
    private final BroadcastService broadcastService;

    @Autowired
    public SeshController(SeshService seshService, StompMessageFactory messageFactory,ModelMapper modelMapper, BroadcastService broadcastService) {

        this.seshService = seshService;
        this.messageFactory = messageFactory;
	this.modelMapper = modelMapper;
	this.broadcastService = broadcastService;
    }

    @SubscribeMapping("/topic/sesh/{seshCode}/controller")
    public StompMessage joinSeshAsController(@Header final String playerName, @DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId, final @Header String reconnectToken) {

        log.info("Started joinSeshAsController with playerName={} seshCode={}, socketId={}, reconectToken={}", playerName, seshCode, socketId, reconnectToken);
        try {
            SeshStateWrapper state = seshService.joinAsController(seshCode, playerName, socketId, reconnectToken);
            StompMessage reply = messageFactory.getMessage(state);
            log.info("Finished joinSeshAsController with playerName={}, seshCode={}, socketId={}, reconectToken={}, reply={}", playerName, seshCode, socketId, reconnectToken, reply);
            return reply;
        } catch (Exception e) {

            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsHost(reply={})", reply);
            return reply;
        }

    }

    @SubscribeMapping("/topic/sesh/{seshCode}/host")
    public StompMessage joinSeshAsHost(@DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId, final @Header String reconnectToken) {

        log.info("StompControllerImpl: Entering joinSeshAsHost(seshCode={}, socketId={}, reconectToken={})", seshCode, socketId, reconnectToken);
        try {
            SeshStateWrapper state = seshService.joinAsHost(seshCode, socketId);
            StompMessage reply = messageFactory.getMessage(state);
            log.info("StompControllerImpl: Exiting joinSesh(reconectToken={}, reply={})", reconnectToken, reply);

            return reply;
        } catch (Exception e) {

            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsHost(reply={})", reply);
            return reply;
        }
    }

    @MessageMapping("/topic/sesh/{seshCode}")
    public StompMessage sendCommandToSesh(final CommandStompMessage message, @DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId) {

        log.info("Entering sendCommandToSesh with message={}, seshCode={}, socketId={}", message, seshCode, socketId);
        try {
            message.getCommand().setPlayerId(socketId);
            this.seshService.sendCommandToSesh(message.getCommand(), seshCode);
            StompMessage reply = messageFactory.getAckMessage();
            log.info("Exiting sendCommandToSesh with reply={}", reply);

            return reply;
        } catch (Exception e) {

            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsHost(reply={})", reply);
            return reply;
        }
    }

    @MessageMapping("/topic/seshs/{seshCode}/broadcasts/state")
    public StompMessage broadcastState(final MessagingSeshUpdate messagingSeshUpdate, @DestinationVariable final String seshCode) {

        try {
            log.info("Started broadcastToSesh with seshCode={}, MessagingSeshUpdate={}", seshCode, messagingSeshUpdate);
		    SeshUpdate seshUpdate = modelMapper.map(messagingSeshUpdate, SeshUpdate.class);
		    broadcastService.broadcastToSesh(seshCode, seshUpdate);
		    log.info("Finished broadcastToSesh with seshCode={}, MessagingSeshUpdate={}", seshCode, messagingSeshUpdate);
            return messageFactory.getAckMessage();
        } catch (Exception e) {

            StompMessage reply = messageFactory.getMessage(e);
            log.error("StompControllerImpl: Exiting joinSeshAsHost(reply={})", reply);
            return reply;
        }
    }
}
