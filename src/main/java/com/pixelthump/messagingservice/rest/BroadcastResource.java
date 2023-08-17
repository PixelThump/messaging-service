package com.pixelthump.messagingservice.rest;
import com.pixelthump.messagingservice.repository.model.Role;
import com.pixelthump.messagingservice.rest.model.MessagingDifferentBroadcastRequest;
import com.pixelthump.messagingservice.rest.model.MessagingMultiBroadcastRequest;
import com.pixelthump.messagingservice.rest.model.MessagingRoleBroadcastRequest;
import com.pixelthump.messagingservice.rest.model.MessagingSinglePlayerBroadcastRequest;
import com.pixelthump.messagingservice.service.BroadcastService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seshs/{seshCode}/broadcasts")
@Log4j2
public class BroadcastResource {

    private final BroadcastService broadcastService;

    @Autowired
    public BroadcastResource(BroadcastService broadcastService) {

        this.broadcastService = broadcastService;
    }

    @PostMapping("/multiple")
    public void broadcastToListOfPlayers(@PathVariable String seshCode, @RequestBody MessagingMultiBroadcastRequest request) {

        log.info("Started broadcastToListOfPlayers with seshCode={}, request={}", seshCode, request);
        broadcastService.broadcastToListOfPlayers(seshCode, request.recipients(), request.payload());
        log.info("Finished broadcastToListOfPlayers with seshCode={}, request={}", seshCode, request);
    }

    @PostMapping("/different")
    public void broadcastToDifferentPlayers(@PathVariable String seshCode, @RequestBody MessagingDifferentBroadcastRequest request) {

        log.info("Started broadcastToDifferentPlayers with seshCode={}, request={}", seshCode, request);
        broadcastService.broadcastToDifferentPlayers(seshCode, request.playerNameToPayload());
        log.info("Finished broadcastToDifferentPlayers with seshCode={}, request={}", seshCode, request);
    }

    @PostMapping("/player")
    public void broadcastToSinglePlayer(@PathVariable String seshCode, @RequestBody MessagingSinglePlayerBroadcastRequest request) {

        log.info("Started broadcastToSinglePlayer with seshCode={}, request={}", seshCode, request);
        broadcastService.broadcastToSinglePlayer(seshCode, request.playerName(), request.payload());
        log.info("Finished broadcastToSinglePlayer with seshCode={}, request={}", seshCode, request);
    }

    @PostMapping()
    public void broadcastToAllPlayers(@PathVariable String seshCode, @RequestBody Object payload) {

        log.info("Started broadcastToAllPlayers with seshCode={}, payload={}", seshCode, payload);
        broadcastService.broadcastToAllPlayers(seshCode, payload);
        log.info("Finished broadcastToAllPlayers with seshCode={}, payload={}", seshCode, payload);
    }

    @PostMapping("/role")
    public void broadcastToRole(@PathVariable String seshCode, @RequestBody MessagingRoleBroadcastRequest request) {

        log.info("Started broadcastToRole with seshCode={}, request={}", seshCode, request);
        broadcastService.broadcastToRole(seshCode, Role.valueOf(request.role().name()), request.payload());
        log.info("Finished broadcastToRole with seshCode={}, request={}", seshCode, request);
    }

}
