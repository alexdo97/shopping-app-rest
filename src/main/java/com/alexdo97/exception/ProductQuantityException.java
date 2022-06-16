package com.alexdo97.exception;

public class ProductQuantityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductQuantityException(String errorMessage) {
		super(errorMessage);
	}

}
