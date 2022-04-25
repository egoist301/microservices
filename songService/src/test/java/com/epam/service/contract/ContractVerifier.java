package com.epam.service.contract;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.SongApplication;
import com.epam.controller.SongController;
import com.epam.domain.Song;
import com.epam.service.impl.SongServiceImpl;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(classes = SongApplication.class)
public class ContractVerifier {

  @Autowired SongController songController;

  @MockBean SongServiceImpl songService;

  @BeforeEach
  public void setup() {
    RestAssuredMockMvc.standaloneSetup(songController);
    var song =
        Song.builder()
            .name("We Are the Champions")
            .artist("Queen")
            .album("News of the World")
            .length("3:14")
            .resourceId(7L)
            .year(1977)
            .build();
    Mockito.when(songService.save(song)).thenReturn(1L);
  }
}
