package com.epam.service.component.api;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.epam.domain.Song;


@Component
@Scope(SCOPE_CUCUMBER_GLUE) // scope tells Cucumber to remove this bean and recreate a new one if
// needed after each scenario
public class SongHttpClient {

  private final String SERVER_URL = "http://localhost";
  private final String ENDPOINT = "/songs";
  private final RestTemplate restTemplate = new RestTemplate();
  @LocalServerPort private int port;

  private String getSongsEndpoint() {
    return SERVER_URL + ":" + port + ENDPOINT;
  }

  public ResponseEntity<Map> post(final Song Song) {
    return restTemplate.postForEntity(getSongsEndpoint(), Song, Map.class);
  }

  public ResponseEntity<Song> getById(Object id) {
    return restTemplate.getForEntity(getSongsEndpoint() + "/" + id, Song.class);
  }

  public void delete(List<Long> ids) {
    String param = ids.stream().map(Object::toString).collect(Collectors.joining(","));
    UriComponents builder = UriComponentsBuilder
        .fromUriString(getSongsEndpoint())
        .queryParam("ids", param)
        .build();
    restTemplate.delete(builder.toUriString(), Map.of("ids", param));
  }
}
