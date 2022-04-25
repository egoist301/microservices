package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.domain.Song;
import com.epam.repository.SongRepository;
import com.epam.service.impl.SongServiceImpl;

@ExtendWith(MockitoExtension.class)
class SongServiceTest {

  @Mock private SongRepository songRepository;
  @InjectMocks private SongServiceImpl songService;

  @Test
  void findById_returnSong() {
    // given
    final long id = 1L;

    Song song = mock(Song.class);

    when(songRepository.findById(id)).thenReturn(Optional.of(song));
    // when
    Song actualSong = songService.findById(id);
    // then
    assertEquals(song, actualSong);
  }

  @Test
  void save_returnGeneratedId() {
    // given
    final long id = 1L;

    Song newSong = mock(Song.class);
    Song expectedSong = new Song();
    expectedSong.setId(id);

    when(songRepository.save(newSong)).thenReturn(expectedSong);
    // when
    Long expectedId = songService.save(newSong);
    // then
    assertEquals(expectedId, id);
  }

  @Test
  void delete_twoIds_returnSingletonArray() {
    // given
    List<Long> inputIds = List.of(1L, 2L);
    Long[] expectedIds = new Long[] {1L};
    Song song = new Song();
    song.setId(1L);

    when(songRepository.deleteAllByIdIn(inputIds)).thenReturn(List.of(song));
    // when
    Long[] actualIds = songService.delete(inputIds);
    // then
    assertArrayEquals(expectedIds, actualIds);
  }
}
