package com.epam.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.epam.service.impl.ResourceProcessorServiceImpl;

@ExtendWith(MockitoExtension.class)
class ResourceProcessorServiceTest {
  @Autowired private ResourceProcessorServiceImpl resourceProcessorService;

  @Test
  void retrieveResourceMetadata() {

  }
}
