package com.pixelthump.messagingservice.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixelthump.messagingservice.rest.model.MessagingSeshUpdate;
import com.pixelthump.messagingservice.service.BroadcastService;
import com.pixelthump.messagingservice.service.model.SeshUpdate;

@RestController
@RequestMapping("/seshs/{seshCode}/broadcasts")
public class BroadcastResource {

	private final ModelMapper modelMapper;
	private final BroadcastService broadcastService;

	@Autowired
	public BroadcastResource(ModelMapper modelMapper, BroadcastService broadcastService) {
		this.modelMapper = modelMapper;
		this.broadcastService = broadcastService;
	}

	@PostMapping
	public void broadcastToSesh(@PathVariable String seshCode, MessagingSeshUpdate messagingSeshUpdate) {

		SeshUpdate seshUpdate = modelMapper.map(messagingSeshUpdate, SeshUpdate.class);
		broadcastService.broadcastToSesh(seshCode, seshUpdate);
	}

}
