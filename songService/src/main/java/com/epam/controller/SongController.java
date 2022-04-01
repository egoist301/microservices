package com.epam.controller;

import java.util.Arrays;
import java.util.Map;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.domain.Song;
import com.epam.service.SongService;

import lombok.RequiredArgsConstructor;

@Validated
@RequestMapping("/songs")
@RestController
@RequiredArgsConstructor
public class SongController {
  private final SongService songService;

  @GetMapping("/{id}")
  public ResponseEntity<Song> get(@PathVariable Long id) {
    return ResponseEntity.ok(songService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Map<String, Long>> create(@Valid @RequestBody Song song) {
    return ResponseEntity.ok(Map.of("id", songService.save(song)));
  }

  @DeleteMapping(params = "ids")
  public ResponseEntity<Map<String, Long[]>> delete(@RequestParam @Length(max = 200) String ids) {
    var deletedIds =
        songService.delete(Arrays.stream(ids.split(",")).map(Long::parseLong).toList());
    return ResponseEntity.ok(Map.of("ids", deletedIds));
  }
}
