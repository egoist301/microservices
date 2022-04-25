package com.epam.service.component.step.context;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class DeleteScenarioContext {
  private List<Long> existedSongIds;
  private ResponseEntity<Map> response;
}
