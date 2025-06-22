package com.hotel.reservation.exception;

import com.hotel.reservation.api.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.validation.ConstraintViolationException;

import java.io.IOException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	@ExceptionHandler(value
			= { java.time.format.DateTimeParseException.class, ConstraintViolationException.class, NoSuchElementException.class, NumberFormatException.class, IllegalArgumentException.class, IllegalStateException.class})
	protected ResponseEntity<Object> badeRequest(
			RuntimeException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setCode("400");
		errorResponse.setMessage(ex.getMessage());
		return handleExceptionInternal(ex, errorResponse,
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value
			= { IOException.class, DataIntegrityViolationException.class, Exception.class, Throwable.class })
	protected ResponseEntity<Object> internalError(
			RuntimeException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, "",
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
