package com.flight.baggagehandlerservice.exception;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.PropertyAccessException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.flight.baggagehandlerservice.model.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, List<String>> errorMessageMap = ex.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.groupingBy(FieldError::getField,
						Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())));
		errorMessageMap.putAll(ex.getBindingResult().getGlobalErrors().stream()
				.collect(Collectors.groupingBy(ObjectError::getObjectName,
						Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList()))));
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorMessageMap);
		printExceptionLog(ex);
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ErrorResponse> handleTypeMismatchtException(MethodArgumentTypeMismatchException ex) {
		Class<?> reqType = ex.getRequiredType();
		String error = ex.getName() + " should be of type " + (reqType == null ? "Object" : reqType.getSimpleName());
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), Map.of(ex.getName(), List.of(error)));
		printExceptionLog(ex);
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	public ResponseEntity<ErrorResponse> handleConstraintViolation(MissingServletRequestParameterException ex) {
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
				Map.of(ex.getParameterName(), List.of("Invalid Request Parameter")));
		printExceptionLog(ex);
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { InvalidFormatException.class })
	public ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
		String errorField = !ex.getPath().isEmpty() ? ex.getPath().get(0).getFieldName() : "Invalid Parameter";
		Class<?> reqType = ex.getTargetType();
		String error = errorField + " should be of type " + (reqType == null ? "Object" : reqType.getSimpleName());
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), Map.of(errorField, List.of(error)));
		printExceptionLog(ex);
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { JsonProcessingException.class })
	public ResponseEntity<ErrorResponse> handleRequestJsonParsingException(JsonProcessingException ex) {
		String errorField = "Invalid Parameter";
		if (ex.getProcessor() != null && ex.getProcessor() instanceof JsonParser) {
			errorField = ((JsonParser) ex.getProcessor()).getParsingContext().getCurrentName();
		}
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
				Map.of(errorField, List.of(errorField)));
		printExceptionLog(ex);
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ BindException.class })
	public ResponseEntity<Object> handleBindException(BindException ex) {
		Map<String, List<String>> errorMessageMap = ex.getBindingResult().getFieldErrors().stream().collect(Collectors
				.groupingBy(FieldError::getField, Collectors.mapping(this::getErrorMessage, Collectors.toList())));
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorMessageMap);
		printWarningLog(ex);
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ NoSuchElementException.class })
	public ResponseEntity<Object> handleBindException(NoSuchElementException ex) {
		String errorField = "Invalid Parameter";
		var apiException = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(),
				Map.of(errorField, List.of(ex.getMessage())));
		return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
	}

	private String getErrorMessage(FieldError fieldError) {
		String errorMessage = fieldError.getDefaultMessage();
		if (fieldError.contains(PropertyAccessException.class)) {
			errorMessage = Objects.requireNonNull(fieldError.unwrap(PropertyAccessException.class).getRootCause())
					.getMessage();
		}
		return errorMessage;
	}

	private void printExceptionLog(Exception ex) {
		log.error("Error : {}", ex.getMessage());
	}

	private void printWarningLog(Exception ex) {
		log.warn("Warning : {}", ex.getMessage());
	}
}
