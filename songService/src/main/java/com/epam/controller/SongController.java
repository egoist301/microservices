package com.epam.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
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

import lombok.AllArgsConstructor;

@RestController
@Validated
@RequestMapping("/songs")
@AllArgsConstructor
public class SongController {
  private SongService songService;

  @GetMapping("/{id}")
  public ResponseEntity<Song> get(@PathVariable Long id) {
    return new ResponseEntity<>(songService.findById(id), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Long> create(@Valid @RequestBody Song song) {
    return new ResponseEntity<>(songService.save(song), HttpStatus.CREATED);
  }

  @DeleteMapping(params = "ids")
  public ResponseEntity<Long[]> delete(@RequestParam @Length(max = 200) String ids) {
    return new ResponseEntity<>(
        songService.delete(
            Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList())),
        HttpStatus.OK);
  }
}
