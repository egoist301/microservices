package com.epam.e2e.api.v1.impl;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourcesStepDefinitions {

  private static final String RESOURCE_ENDPOINT_TEMPLATE = "http://localhost:%d/resources";
  private static final String SONG_SERVICE_ENDPOINT = "songs";

  @LocalServerPort private int port;

  @Value("${song-service.url}")
  private String songServiceUrl;

  @When("client calls post endpoint to save resource")
  public void clientCallsPostEndpointToSave() {
    ResponseEntity<Map> postResponse = postResource();
    assertEquals(200, postResponse.getStatusCodeValue());
  }

  @Then("client calls song service to retrieve processed metadata")
  public void clientCallsSongServiceToRetrieveProcessedMetadata() {
    await().untilAsserted(() -> assertEquals(200, getSongMetadata().statusCode()));
  }

  public org.springframework.core.io.Resource getMp3File() {
    return new ClassPathResource("sample-3s.mp3");
  }

  private ResponseEntity<Map> postResource() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", getMp3File());
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    return new RestTemplate()
        .postForEntity(String.format(RESOURCE_ENDPOINT_TEMPLATE, port), requestEntity, Map.class);
  }

  private HttpResponse<String> getSongMetadata()
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(new URI(songServiceUrl + SONG_SERVICE_ENDPOINT + "/1"))
            .GET()
            .build();
    return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
  }
}
