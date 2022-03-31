package com.epam.service;

import java.util.List;

import com.epam.domain.Song;

public interface SongService {
  Song findById(Long id);

  Long save(Song song);

  Long[] delete(List<Long> ids);
}
