package com.alexdo97.exception;

public class TotalCartValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TotalCartValueException(String errorMessage) {
		super(errorMessage);
	}
}
