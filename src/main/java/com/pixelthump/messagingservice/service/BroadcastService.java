package com.pixelthump.messagingservice.service;

import com.pixelthump.messagingservice.service.model.SeshUpdate;

public interface BroadcastService {

	void broadcastToSesh(SeshUpdate seshUpdate);
}
