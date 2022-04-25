package com.epam.contract;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import com.epam.controller.ResourceController;
import com.epam.service.ResourceCreatedQueueProducerService;
import com.epam.service.ResourceService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMessageVerifier
public class BaseContractTest {

  @Autowired private ResourceController resourceController;
  @MockBean private ResourceService resourceService;
  @Autowired private ResourceCreatedQueueProducerService resourceCreatedQueueProducerService;

  @BeforeEach
  public void setup() throws IOException {
    StandaloneMockMvcBuilder standaloneMockMvcBuilder =
        MockMvcBuilders.standaloneSetup(resourceController);
    RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
    Mockito.when(resourceService.findById(1L)).thenReturn(getMp3FileBytes());
  }

  public void sendResourceIdToQueue() {
    resourceCreatedQueueProducerService.produce(1L);
  }

  private byte[] getMp3FileBytes() throws IOException {
    return new ClassPathResource("sample-3s.mp3").getInputStream().readAllBytes();
  }
}
