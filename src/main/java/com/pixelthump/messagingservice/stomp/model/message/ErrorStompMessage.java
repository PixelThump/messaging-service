package com.pixelthump.messagingservice.stomp.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ErrorStompMessage implements StompMessage {

	private String error;
}