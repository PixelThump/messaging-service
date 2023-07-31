package com.pixelthump.messagingservice.stomp;
import com.pixelthump.messagingservice.service.SeshService;
import com.pixelthump.messagingservice.service.StompMessageFactory;
import com.pixelthump.messagingservice.service.model.SeshState;
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

    @Autowired
    public SeshController(SeshService seshService, StompMessageFactory messageFactory, ModelMapper modelMapper) {

        this.seshService = seshService;
        this.messageFactory = messageFactory;
        this.modelMapper = modelMapper;
    }

    @SubscribeMapping("/topic/sesh/{seshCode}/controller")
    public StompMessage joinSeshAsController(@Header final String playerName, @DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId) {

        log.info("Started joinSeshAsController with playerName={} seshCode={}, socketId={}", playerName, seshCode, socketId);
        SeshState state = seshService.joinAsController(seshCode, playerName, socketId);
        StompMessage reply = messageFactory.getMessage(state);
        log.info("Finished joinSeshAsController with playerName={}, seshCode={}, socketId={}, reply={}", playerName, seshCode, socketId, reply);

        return reply;

    }

    @SubscribeMapping("/topic/sesh/{seshCode}/host")
    public StompMessage joinSeshAsHost(@DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId) {

        log.info("StompControllerImpl: Entering joinSeshAsHost(seshCode={}, socketId={})", seshCode, socketId);
        SeshState state = seshService.joinAsHost(seshCode, socketId);
        StompMessage reply = messageFactory.getMessage(state);
        log.info("StompControllerImpl: Exiting joinSesh(reply={})", reply);

        return reply;
    }

    @MessageMapping("/topic/sesh/{seshCode}")
    public StompMessage sendCommandToSesh(final CommandStompMessage message, @DestinationVariable final String seshCode, final @Header("simpSessionId") String socketId) {

        log.info("Entering sendCommandToSesh with message={}, seshCode={}, socketId={}", message, seshCode, socketId);
        message.getCommand().setPlayerId(socketId);
        com.pixelthump.messagingservice.service.model.message.CommandStompMessage commandStompMessage = modelMapper.map(message, com.pixelthump.messagingservice.service.model.message.CommandStompMessage.class);
        this.seshService.sendCommandToSesh(commandStompMessage, seshCode);
        StompMessage reply = messageFactory.getAckMessage();
        log.info("Exiting sendCommandToSesh with reply={}", reply);

        return reply;
    }
}