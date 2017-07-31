/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.fringecommon.guiutilities;

public class Message {

	public enum MessageType {
		INFO, ERROR
	}

	private MessageType messageType;

	private String text;

	public Message(MessageType messageType, String text) {
		super();
		this.messageType = messageType;
		this.text = text;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public String getText() {
		return text;
	}

}
