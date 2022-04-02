package com.epam.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
  byte[] getFileBytesByKey(String key);

  String save(Long id, MultipartFile file);

  void deleteByFileKeys(List<String> keys);
}
