package com.pixelthump.messagingservice.stomp;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.pixelthump.messagingservice.service.SeshService;
import com.pixelthump.messagingservice.service.model.SeshState;
import com.pixelthump.messagingservice.stomp.model.message.CommandStompMessage;
import com.pixelthump.messagingservice.stomp.model.message.StompMessage;

@Controller
@Log4j2
public class SeshController {

	private final SeshService seshService;
	private final StompMessageFactory messageFactory;

	@Autowired
	public SeshController(SeshService seshService, StompMessageFactory messageFactory) {
		this.seshService = seshService;
		this.messageFactory = messageFactory;
	}

	@SubscribeMapping("/topic/sesh/{seshCode}/controller")
	public StompMessage joinSeshAsController(@Header final String playerName, @DestinationVariable final String seshCode,
		final @Header("simpSessionId") String socketId) {

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
	public StompMessage sendCommandToSesh(final CommandStompMessage message, @DestinationVariable final String seshCode,
		final @Header("simpSessionId") String socketId) {

		log.info("Entering sendCommandToSesh with message={}, seshCode={}, socketId={}", message, seshCode, socketId);

		message.getCommand().setPlayerId(socketId);
		this.seshService.sendCommandToSesh(message, seshCode);
		StompMessage reply = messageFactory.getAckMessage();
		log.info("Exiting sendCommandToSesh with reply={}", reply);

		return reply;
	}
}