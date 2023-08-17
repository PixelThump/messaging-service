package com.pixelthump.messagingservice.service;
import com.pixelthump.messagingservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeshServiceImpl implements SeshService {

    private final PlayerRepository playerRepository;

    @Autowired
    public SeshServiceImpl(PlayerRepository playerRepository) {

        this.playerRepository = playerRepository;
    }

    @Override
    public void deleteSesh(String seshCode) {

        playerRepository.deleteByPlayerId_SeshCode(seshCode);
    }
}
