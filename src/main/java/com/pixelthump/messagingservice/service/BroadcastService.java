package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.repository.model.Role;

import java.util.List;
import java.util.Map;

public interface BroadcastService {

	void broadcastToListOfPlayers(String seshCode, List<String> recipients, Object payload);

    void broadcastToDifferentPlayers(String seshCode, Map<String, Object> playerNameToPayload);

	void broadcastToSinglePlayer(String seshCode, String playerName, Object payload);

	void broadcastToAllPlayers(String seshCode, Object payload);

	void broadcastToRole(String seshCode, Role role, Object payload);
}
