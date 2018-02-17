package com.qdboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An internal error has occurred.")
public class GenericException extends Exception {

	public GenericException() {
	}

	public GenericException(Exception cause) {
		super(cause);
	}

	public GenericException(String message, Exception cause) {
		super(message, cause);
	}

	public GenericException(String message) {
		super(message);
	}
}