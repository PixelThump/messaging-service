package com.pixelthump.messagingservice.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pixelthump.messagingservice.rest.model.MessagingSeshUpdate;

@RequestMapping("/seshs/{seshCode}/broadcasts")
public class BroadcastResource {

	@PostMapping
	public void broadcast(@PathVariable String seshCode, MessagingSeshUpdate seshUpdate){


	}

}
