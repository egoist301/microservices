package com.epam.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.domain.Song;

public interface SongRepository extends JpaRepository<Song, Long> {
  Collection<Song> deleteAllByIdIn(Collection<Long> ids);
}
