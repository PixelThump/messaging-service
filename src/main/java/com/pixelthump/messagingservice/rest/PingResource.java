package com.pixelthump.messagingservice.rest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.pixelthump.messagingservice.rest.model.Ping;

@RestController
@RequestMapping("/ping")
public class PingResource {

    @GetMapping()
    @ResponseBody
    public Ping ping() {

        return new Ping();
    }

}
