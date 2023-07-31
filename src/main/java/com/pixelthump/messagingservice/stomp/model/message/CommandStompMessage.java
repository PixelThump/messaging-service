package com.pixelthump.messagingservice.stomp.model.message;
import com.pixelthump.messagingservice.stomp.model.Command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CommandStompMessage implements StompMessage {

	Command command;
}