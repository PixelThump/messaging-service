package com.pixelthump.messagingservice.rest;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.service.SeshService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest(classes = Application.class)
class SeshResourceTest {

    @Autowired
    SeshResource seshResource;

    @MockBean
    SeshService seshService;
    @Test
    void deleteSesh() {

        seshResource.deleteSesh("abcd");
        verify(seshService).deleteSesh("abcd");
    }
}