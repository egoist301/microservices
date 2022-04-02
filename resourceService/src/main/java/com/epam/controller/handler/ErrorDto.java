package com.epam.controller.handler;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorDto {
  private HttpStatus status;
  private int code;
  private String message;
}
