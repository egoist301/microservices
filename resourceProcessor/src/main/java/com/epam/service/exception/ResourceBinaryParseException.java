package com.epam.service.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceBinaryParseException extends RuntimeException {
  public ResourceBinaryParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
