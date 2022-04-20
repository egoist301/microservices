package com.epam.service.impl;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.epam.domain.Resource;
import com.epam.repository.ResourceRepository;
import com.epam.service.ResourceCreatedQueueProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceCreatedQueueProducerServiceImpl
    implements ResourceCreatedQueueProducerService {

  private final RabbitTemplate rabbitTemplate;
  private final ResourceRepository resourceRepository;
  private final RabbitTemplateReturnCallback returnCallback;

  @Value("${spring.rabbitmq.exchange-name}")
  private String resourceCreatedEventExchangeName;

  @Override
  public void produce(Long resourceId) {
    rabbitTemplate.setExchange(resourceCreatedEventExchangeName);
    rabbitTemplate.setReturnsCallback(returnCallback);
    rabbitTemplate.convertAndSend(resourceId);
  }

  @Recover
  @Override
  public void recover(RuntimeException e, Long resourceId) {
    resourceRepository
        .findById(resourceId)
        .ifPresentOrElse(
            this::markResourceAsUnsent,
            () ->
                log.error(
                    "The object whose Id({}) was tried to put in the queue was not found.",
                    resourceId));
  }

  private void markResourceAsUnsent(Resource resource) {
    resource.setSent(false);
    resourceRepository.save(resource);
  }

  @Scheduled(fixedRateString = "${spring.rabbitmq.producer.task-retry-delay}")
  @Override
  public void sendUnsentFiles() {
    try {
      List<Resource> resources = resourceRepository.findAllBySentFalse();
      for (Resource resource : resources) {
        produce(resource.getId());
        resource.setSent(true);
        resourceRepository.save(resource);
      }
    } catch (RuntimeException e) {
      log.error("Error sending Ids to the queue", e);
    }
  }
}
