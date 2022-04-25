package com.epam.service.component.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.epam.domain.Song;
import com.epam.service.component.api.SongHttpClient;
import com.epam.service.component.step.context.DeleteScenarioContext;
import com.epam.service.component.step.context.GetByIdScenarioContext;
import com.epam.service.component.step.context.PostScenarioContext;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinition {

  @Autowired private SongHttpClient songHttpClient;
  @Autowired private PostScenarioContext postScenarioContext;
  @Autowired private GetByIdScenarioContext getByIdScenarioContext;
  @Autowired private DeleteScenarioContext deleteScenarioContext;

  @Given("user's song")
  public void generatePostRequestBody() {
    var song =
        Song.builder()
            .name("song_name")
            .artist("artist")
            .album("album")
            .length("03:12")
            .resourceId(2L)
            .year(2022)
            .build();
    postScenarioContext.setRequest(song);
  }

  @When("user uploads song")
  public void uploadSong() {
    var response = songHttpClient.post(postScenarioContext.getRequest());
    postScenarioContext.setResponse(response);
  }

  @Then("save song")
  public void isSongSaved() {
    var id = postScenarioContext.getResponse().getBody().get("id");
    assertNotNull(songHttpClient.getById(id).getBody().getId());
  }

  @Then("return song id")
  public void getSongId() {
    assertEquals(HttpStatus.OK, postScenarioContext.getResponse().getStatusCode());
    assertNotNull(postScenarioContext.getResponse().getBody().get("id"));
  }

  @Given("pre-saved song")
  public void getSavedSong() {
    var song =
        Song.builder()
            .name("song_name")
            .artist("artist")
            .album("album")
            .length("03:12")
            .resourceId(2L)
            .year(2022)
            .build();
    var response = songHttpClient.post(song);
    var id = Long.valueOf((Integer) response.getBody().get("id"));
    song.setId(id);
    getByIdScenarioContext.setExistedSong(song);
  }

  @When("user requests song by id")
  public void requestSongById() {
    var id = getByIdScenarioContext.getExistedSong().getId();
    getByIdScenarioContext.setResponse(songHttpClient.getById(id));
  }

  @Then("return song")
  public void getSong() {
    assertEquals(HttpStatus.OK, getByIdScenarioContext.getResponse().getStatusCode());
    assertEquals(
        getByIdScenarioContext.getExistedSong(), getByIdScenarioContext.getResponse().getBody());
  }

  @Given("pre-saved songs")
  public void getSavedSongs() {
    var song =
        Song.builder()
            .name("song_name")
            .artist("artist")
            .album("album")
            .length("03:12")
            .resourceId(2L)
            .year(2022)
            .build();
    var song2 =
        song.builder()
            .name("song_name2")
            .artist("artist2")
            .album("album2")
            .length("03:12")
            .resourceId(2L)
            .year(2022)
            .build();
    var response = songHttpClient.post(song);
    var id = Long.valueOf((Integer) response.getBody().get("id"));
    var response2 = songHttpClient.post(song2);
    var id2 = Long.valueOf((Integer) response2.getBody().get("id"));
    deleteScenarioContext.setExistedSongIds(List.of(id, id2));
  }

  @When("user request delete songs")
  public void deleteSongs() {
    var ids = deleteScenarioContext.getExistedSongIds();
    try {
      songHttpClient.delete(ids);
      ResponseEntity<Map> response =
          new ResponseEntity<>(Map.of("ids", ids), HttpStatus.NO_CONTENT);
      deleteScenarioContext.setResponse(response);
    } catch (HttpClientErrorException ex) {
      String message = ex.getResponseBodyAsString();
      ResponseEntity<Map> response =
          new ResponseEntity<>(Map.of("message", message), HttpStatus.NO_CONTENT);
      deleteScenarioContext.setResponse(response);
    }
  }

  @Then("return deleted songs ids")
  public void getDeletedSongIds() {
    assertEquals(HttpStatus.NO_CONTENT, deleteScenarioContext.getResponse().getStatusCode());
    assertNotNull(deleteScenarioContext.getResponse().getBody().get("ids"));
  }
}
