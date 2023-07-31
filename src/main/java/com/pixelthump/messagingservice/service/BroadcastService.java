package com.pixelthump.messagingservice.service;

import com.pixelthump.messagingservice.service.model.SeshUpdate;

public interface BroadcastService {

	void broadcastToSesh(String seshCode, SeshUpdate seshUpdate);
}
