package com.pixelthump.messagingservice.repository.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Player {


    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    @EmbeddedId
    private PlayerId playerId;
    @Column(name = "reconnect_token")
    private String reconnectToken;
    @Column(name = "reconnect_failed", nullable = false)
    private boolean reconnectFailed;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return role == player.role && Objects.equals(playerId, player.playerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(role, playerId);
    }
}
