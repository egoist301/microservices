package com.epam.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public interface QueueConsumerService {
  @RabbitListener(queues = "${spring.rabbitmq.queue-name}")
  void receiveMessage(Long resourceId, Message message);
}
