package com.pixelthump.messagingservice.rest;
import com.pixelthump.messagingservice.service.SeshService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seshs")
@Log4j2
public class SeshResource {

    private final SeshService seshService;

    @Autowired
    public SeshResource(SeshService seshService) {

        this.seshService = seshService;
    }

    @DeleteMapping("/{seshCode}")
    public void deleteSesh(@PathVariable final String seshCode) {

        log.info("Entering deleteSesh with seshCode={}", seshCode);
        seshService.deleteSesh(seshCode);
        log.info("Exiting deleteSesh with seshCode={}", seshCode);
    }
}
