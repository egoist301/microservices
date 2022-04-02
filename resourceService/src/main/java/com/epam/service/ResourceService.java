package com.epam.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {
  byte[] findById(Long id);

  Long save(MultipartFile file);

  Long[] delete(List<Long> ids);
}
