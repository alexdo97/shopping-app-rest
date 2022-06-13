package com.alexdo97.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class HttpError extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HttpStatus status;
	private String message;

	public static final String ERROR_MSG_UNKNOWN = "Unknown error. Please contact the support or open a new ticket";

	public static HttpError notFound(String message) {
		return builder().status(HttpStatus.NOT_FOUND).message(message).build();
	}

	public static HttpError badRequest(String message) {
		return builder().status(HttpStatus.BAD_REQUEST).message(message).build();
	}

	public static HttpError internalServerError(String message) {
		return builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(message).build();
	}
}