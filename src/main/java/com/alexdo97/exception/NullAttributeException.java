package com.alexdo97.exception;

public class NullAttributeException extends Exception {

	private static final long serialVersionUID = 1L;

	public NullAttributeException(String errorMessage) {
		super(errorMessage);
	}
}
