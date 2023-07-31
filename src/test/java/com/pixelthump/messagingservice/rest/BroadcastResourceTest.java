package com.pixelthump.messagingservice.rest;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.rest.model.MessagingSeshUpdate;
import com.pixelthump.messagingservice.service.BroadcastService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class BroadcastResourceTest {

    @Autowired
    BroadcastResource resource;
    @MockBean
    BroadcastService broadcastService;
    @Test
    void broadcastToSesh_shouldCallService() {

        MessagingSeshUpdate seshUpdate = new MessagingSeshUpdate();
        resource.broadcastToSesh("sesh", seshUpdate);
        verify(broadcastService).broadcastToSesh(eq("sesh"),any());
    }
}