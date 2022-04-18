package com.epam.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
  @Value("${spring.rabbitmq.template.exchange}")
  private String exchangeName;

  @Bean
  public FanoutExchange exchange() {
    return new FanoutExchange(exchangeName);
  }
}
