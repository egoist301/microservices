package com.epam.integration.api.v1.impl;

import static com.jayway.jsonpath.JsonPath.read;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.ResourceApplication;
import com.epam.repository.ResourceRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;


@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = ResourceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ResourceControllerIntegrationTest {

  @Autowired private MockMvc mvc;
  @Autowired private AmazonS3 amazonS3;
  @Autowired private ResourceRepository resourceRepository;

  @Value("${cloud.aws.s3.bucket-name}")
  private String bucketName;

  @Test
  public void createResource_correctData_SuccessfullyCreated() throws Exception {
    byte[] fileBytes = Files.readAllBytes(getMp3File().getFile().toPath());
    MockMultipartFile file = new MockMultipartFile("file", "sample.mp3", null, fileBytes);
    MvcResult mvcResult =
        mvc.perform(multipart("/resources").file(file))
            .andExpect(status().isOk())
            .andReturn();
    Long resourceId =
        read(getJsonProvider().parse(mvcResult.getResponse().getContentAsString()), "$.id");
    assertTrue(resourceRepository.findById(resourceId).isPresent());
    assertTrue(amazonS3.doesObjectExist(bucketName, resourceId + "/" + "sample.mp3"));
  }

  private org.springframework.core.io.Resource getMp3File() {
    return new ClassPathResource("sample-3s.mp3");
  }

  private JsonProvider getJsonProvider() {
    ObjectMapper objectMapper =
        new ObjectMapper().configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
    return Configuration.builder()
        .jsonProvider(new JacksonJsonProvider(objectMapper))
        .build()
        .jsonProvider();
  }
}
