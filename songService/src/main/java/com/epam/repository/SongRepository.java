package com.epam.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
  List<Song> deleteAllByIdIn(Collection<Long> ids);
}
