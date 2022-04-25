package com.epam.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "song")
public class Song implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotBlank
  @Column(name = "album")
  private String album;

  @NotBlank
  @Column(name = "artist")
  private String artist;

  @NotBlank
  @Column(name = "length")
  private String length;

  @NotBlank
  @Column(name = "name")
  private String name;

  @NotNull
  @Column(name = "resource_id")
  private Long resourceId;

  @NotNull
  @Column(name = "year")
  private Integer year;
}
