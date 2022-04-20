package com.epam.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {

  @Value("${spring.rabbitmq.queue-name}")
  private String resourceCreatedEventQueueName;

  @Value("${spring.rabbitmq.exchange-name}")
  private String resourceCreatedEventExchangeName;

  @Value("${spring.rabbitmq.dead-letter-queue-name}")
  private String resourceCreatedEventDeadLetterQueueName;

  @Value("${spring.rabbitmq.dead-letter-exchange-name}")
  private String resourceCreatedEventDeadLetterExchangeName;

  @Value("${spring.rabbitmq.dead-letter-queue-ttl}")
  private Long deadLetterQueueTtl;

  @Value("${spring.rabbitmq.parking-lot-queue-name}")
  private String resourceCreatedEventParkingLotQueueName;

  @Value("${spring.rabbitmq.parking-lot-exchange-name}")
  private String resourceCreatedEventParkingLotExchangeName;

  @Bean
  public Queue resourceCreatedEventQueue() {
    return QueueBuilder.durable(resourceCreatedEventQueueName)
        .withArgument("x-dead-letter-exchange", resourceCreatedEventDeadLetterExchangeName)
        .build();
  }

  @Bean
  public FanoutExchange resourceCreatedEventExchange() {
    return new FanoutExchange(resourceCreatedEventExchangeName);
  }

  @Bean
  public Binding resourceCreatedEventQueueToExchangeBinding(
      Queue resourceCreatedEventQueue, FanoutExchange resourceCreatedEventExchange) {
    return BindingBuilder.bind(resourceCreatedEventQueue).to(resourceCreatedEventExchange);
  }

  @Bean
  public Queue resourceCreatedEventDeadLetterQueue() {
    return QueueBuilder.durable(resourceCreatedEventDeadLetterQueueName)
        .withArgument("x-dead-letter-exchange", resourceCreatedEventExchangeName)
        .withArgument("x-message-ttl", deadLetterQueueTtl)
        .build();
  }

  @Bean
  public FanoutExchange resourceCreatedEventDeadLetterExchange() {
    return new FanoutExchange(resourceCreatedEventDeadLetterExchangeName);
  }

  @Bean
  public Binding resourceCreatedEventDeadLetterQueueToExchangeBinding(
      Queue resourceCreatedEventDeadLetterQueue,
      FanoutExchange resourceCreatedEventDeadLetterExchange) {
    return BindingBuilder.bind(resourceCreatedEventDeadLetterQueue)
        .to(resourceCreatedEventDeadLetterExchange);
  }

  @Bean
  public Queue resourceCreatedEventParkingLotQueue() {
    return QueueBuilder.durable(resourceCreatedEventParkingLotQueueName).build();
  }

  @Bean
  public FanoutExchange resourceCreatedEventParkingLotExchange() {
    return new FanoutExchange(resourceCreatedEventParkingLotExchangeName);
  }

  @Bean
  public Binding resourceCreatedParkingLotLetterQueueToExchangeBinding(
      Queue resourceCreatedEventParkingLotQueue,
      FanoutExchange resourceCreatedEventParkingLotExchange) {
    return BindingBuilder.bind(resourceCreatedEventParkingLotQueue)
        .to(resourceCreatedEventParkingLotExchange);
  }

  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
  }

  @Bean
  public MessageHandlerMethodFactory messageHandlerMethodFactory() {
    DefaultMessageHandlerMethodFactory messageHandlerMethodFactory =
        new DefaultMessageHandlerMethodFactory();
    messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
    return messageHandlerMethodFactory;
  }

  @Bean
  public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
    return new MappingJackson2MessageConverter();
  }
}
