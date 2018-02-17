package com.qdboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The project does not exist.")
public class ProjectDoesNotExistsException extends GenericException{
	
}
