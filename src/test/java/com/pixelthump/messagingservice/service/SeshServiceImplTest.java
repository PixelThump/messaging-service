package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;

@SpringBootTest(classes = Application.class)
class SeshServiceImplTest {

    @Autowired
    SeshService seshService;

    @MockBean
    PlayerRepository playerRepository;

    @Test
    void deleteSesh() {

        seshService.deleteSesh("abcd");
        verify(playerRepository).deleteByPlayerId_SeshCode("abcd");
    }
}