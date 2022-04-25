package com.epam.service.integration;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.epam.controller.SongController;
import com.epam.domain.Song;

@SpringBootTest
@AutoConfigureMockMvc
public class SongControllerIntegrationTest {

  private static final String GET_BY_ID_URI = "/songs/{id}";

  @Autowired private MockMvc mockMvc;

  @MockBean private SongController songController;

  @Test
  public void testGetById_songExist_Ok() throws Exception {
    // given
    var song = new Song(1L, "song_name", "artist", "album", "03:12", 2L, 2022);
    given(songController.get(song.getId())).willReturn(ResponseEntity.ok(song));

    // when-then
    this.mockMvc
        .perform(MockMvcRequestBuilders.get(GET_BY_ID_URI, 1).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
  }

  @Test
  public void testGetById_songNotExist_NotFound() throws Exception {
    // given
    given(songController.get(1L)).willThrow(NoSuchElementException.class);

    // when-then
    this.mockMvc
        .perform(MockMvcRequestBuilders.get(GET_BY_ID_URI, 1).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetById_internalError_ServerError() throws Exception {
    // given
    given(songController.get(1L)).willThrow(RuntimeException.class);

    // when-then
    this.mockMvc
        .perform(MockMvcRequestBuilders.get(GET_BY_ID_URI, 1).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is5xxServerError());
  }
}
