package com.epam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SongMetadata {
  private String album;
  private String artist;
  private String length;
  private String name;
  private Long resourceId;
  private Integer year;
}
