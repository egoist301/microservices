package com.epam.service.impl;

import java.math.BigInteger;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.epam.repository.ResourceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitTemplateReturnCallback implements RabbitTemplate.ReturnsCallback {

  private final ResourceRepository resourceRepository;

  @Override
  public void returnedMessage(ReturnedMessage returnedMessage) {
    Long resourceId = bytesToLong(returnedMessage.getMessage().getBody());
    resourceRepository
        .findById(resourceId)
        .ifPresentOrElse(
            resource1 -> {
              resource1.setSent(false);
              resourceRepository.save(resource1);
            },
            () ->
                log.error(
                    "The object whose Id({}) was tried to put in the queue was not found.",
                    resourceId));
  }

  private long bytesToLong(byte[] bytes) {
    return new BigInteger(bytes).longValue();
  }
}
