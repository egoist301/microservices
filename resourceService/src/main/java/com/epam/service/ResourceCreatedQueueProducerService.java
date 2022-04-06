package com.epam.service;

public interface ResourceCreatedQueueProducerService {

  void produce(Long resourceId);

  void recover(RuntimeException e, Long resourceId);

  void sendUnsentFiles();
}
