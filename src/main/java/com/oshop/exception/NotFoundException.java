package com.oshop.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1875788336119918796L;

	public NotFoundException(String message) {
		super(message);
	}
}
