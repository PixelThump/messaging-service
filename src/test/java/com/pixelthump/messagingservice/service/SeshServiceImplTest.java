package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.Application;
import com.pixelthump.messagingservice.service.model.Player;
import com.pixelthump.messagingservice.service.model.SeshInfo;
import com.pixelthump.messagingservice.service.model.SeshState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
class SeshServiceImplTest {

    @Autowired
    SeshService seshService;
    @MockBean
    RestTemplate restTemplate;

    @Test
    void joinSeshAsController_shouldCallRestTemplate() {

        SeshInfo actualSeshinfo = new SeshInfo();
        actualSeshinfo.setSeshCode("abcd");
        actualSeshinfo.setSeshType("quizxel");
        ResponseEntity<SeshInfo> seshinfo = new ResponseEntity<>(actualSeshinfo, HttpStatusCode.valueOf(200));
        when(restTemplate.getForEntity("https://pixelthump.win/api/seshservice/seshs/abcd", SeshInfo.class)).thenReturn(seshinfo);

        ResponseEntity<SeshState> responseEntity = new ResponseEntity<>(new SeshState() {

        }, HttpStatusCode.valueOf(200));
        when(restTemplate.postForEntity("https://pixelthump.win/api/quizxel/seshs/abcd/players/controller", new Player("abcd", "abcd"), SeshState.class)).thenReturn(responseEntity);
        seshService.joinAsController("abcd", "abcd", "abcd");
        verify(restTemplate, times(1)).getForEntity("https://pixelthump.win/api/seshservice/seshs/abcd", SeshInfo.class);
    }
}