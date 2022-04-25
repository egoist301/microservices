package com.epam.service.contract;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.epam.client.ResourceServiceClient;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureStubRunner(
    ids = {"com.epam:resource-service:+:stubs:8090"},
    stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ConsumerContractTest {

  @Autowired private StubTrigger stubTrigger;
  @MockBean private ResourceServiceClient resourceServiceClient;

  @Test
  public void receiveMessage_listenToRabbit_correctFormat() {
    stubTrigger.trigger("resource-created");
    verify(resourceServiceClient).getResourceBinaryData(1L);
  }

  @Test
  public void receiveMessage_resourceServiceRequest_correctResponseCode() {
    ResponseEntity<byte[]> response =
        new RestTemplate().getForEntity("http://localhost:8090/resources/1", byte[].class);
    assertEquals(200, response.getStatusCodeValue());
  }
}