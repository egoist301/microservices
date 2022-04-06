package com.epam.controller.handler;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.epam.service.exception.EntityNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return createResponse(
        new RuntimeException(ex.getBindingResult().getFieldError().getDefaultMessage(), ex),
        status);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class})
  public ResponseEntity<Object> handlePath(Exception e) {
    return createResponse(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException e) {
    return createResponse(e, HttpStatus.NO_CONTENT);
  }

  private ResponseEntity<Object> createResponse(Exception ex, HttpStatus status) {
    ErrorDto error = new ErrorDto();
    error.setStatus(status);
    error.setCode(status.value());
    error.setMessage(ex.getMessage());
    return new ResponseEntity<>(error, new HttpHeaders(), status);
  }
}
