package com.pixelthump.messagingservice.rest;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.pixelthump.messagingservice.rest.model.MessagingSeshUpdate;
import com.pixelthump.messagingservice.service.BroadcastService;
import com.pixelthump.messagingservice.service.model.SeshUpdate;

@RestController
@RequestMapping("/seshs/{seshCode}/broadcasts")
@Log4j2
public class BroadcastResource {

	private final ModelMapper modelMapper;
	private final BroadcastService broadcastService;

	@Autowired
	public BroadcastResource(ModelMapper modelMapper, BroadcastService broadcastService) {
		this.modelMapper = modelMapper;
		this.broadcastService = broadcastService;
	}

	@PostMapping
	public void broadcastToSesh(@PathVariable String seshCode, @RequestBody MessagingSeshUpdate messagingSeshUpdate) {

		log.info("Started broadcastToSesh with seshCode={}, MessagingSeshUpdate={}", seshCode, messagingSeshUpdate);
		SeshUpdate seshUpdate = modelMapper.map(messagingSeshUpdate, SeshUpdate.class);
		broadcastService.broadcastToSesh(seshCode, seshUpdate);
		log.info("Finished broadcastToSesh with seshCode={}, MessagingSeshUpdate={}", seshCode, messagingSeshUpdate);
	}

}
