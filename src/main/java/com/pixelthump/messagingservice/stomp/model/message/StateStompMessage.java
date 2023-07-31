package com.pixelthump.messagingservice.stomp.model.message;

import com.pixelthump.messagingservice.service.model.SeshState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StateStompMessage implements StompMessage {

    SeshState state;
}