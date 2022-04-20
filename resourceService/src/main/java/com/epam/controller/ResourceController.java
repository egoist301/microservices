package com.epam.controller;

import java.util.Arrays;
import java.util.Map;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.epam.service.ResourceService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/resources")
@RestController
@RequiredArgsConstructor
public class ResourceController {
  private final ResourceService resourceService;

  @GetMapping(value = "/{id}")
  public ResponseEntity<byte[]> get(@PathVariable Long id) {
    return ResponseEntity.ok(resourceService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Map<String, Long>> create(@RequestParam("file") MultipartFile file) {
    String originalFileName = file.getOriginalFilename();
    if (originalFileName == null || !originalFileName.endsWith(".mp3")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(Map.of("id", resourceService.save(file)));
  }

  @DeleteMapping(params = "ids")
  public ResponseEntity<Map<String, Long[]>> delete(@RequestParam @Length(max = 200) String ids) {
    var deletedIds =
        resourceService.delete(Arrays.stream(ids.split(",")).map(Long::parseLong).toList());
    return ResponseEntity.ok(Map.of("ids", deletedIds));
  }
}
