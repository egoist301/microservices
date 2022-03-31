package com.epam.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.domain.Song;
import com.epam.repository.SongRepository;
import com.epam.service.SongService;
import com.epam.service.exception.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SongServiceImpl implements SongService {
  private SongRepository songRepository;

  @Override
  public Song findById(Long id) {
    return songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
  }

  @Override
  public Long save(Song song) {
    return songRepository.save(song).getId();
  }

  @Transactional
  @Override
  public Long[] delete(List<Long> ids) {
    return songRepository.deleteAllByIdIn(ids).stream().map(Song::getId).toArray(Long[]::new);
  }
}
