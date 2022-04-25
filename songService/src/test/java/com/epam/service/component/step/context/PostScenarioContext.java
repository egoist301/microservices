package com.epam.service.component.step.context;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.epam.domain.Song;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
public class PostScenarioContext {
  private Song request;
  private ResponseEntity<Map> response;
}
