package com.epam.service.component.step.context;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.epam.domain.Song;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class GetByIdScenarioContext {
  private Song existedSong;
  private ResponseEntity<Song> response;
}
