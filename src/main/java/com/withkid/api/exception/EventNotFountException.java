package com.withkid.api.exception;

@SuppressWarnings("serial")
public class EventNotFountException extends RuntimeException {

	public EventNotFountException() {
		super("이벤트가 존재하지 않습니다.");
	}
}
