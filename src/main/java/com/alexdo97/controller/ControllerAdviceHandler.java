package com.alexdo97.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alexdo97.exception.HttpError;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ControllerAdviceHandler {

	@ExceptionHandler(HttpError.class)
	public ResponseEntity<ErrorResponse> handleHttpError(HttpError e) {
		log.error("Got HttpError Status={} Message={},", e.getStatus().toString(), e.getMessage(), e);
		return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder().message(e.getMessage()).build());
	}

	@Data
	@Builder
	public static class ErrorResponse {
		private String message;
	}
}
