package com.epam.service;

import com.epam.model.SongMetadata;

public interface ResourceProcessorService {
  SongMetadata retrieveResourceMetadata(byte[] fileContent, Long resourceId);
}
