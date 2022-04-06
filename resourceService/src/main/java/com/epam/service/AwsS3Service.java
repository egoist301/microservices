package com.epam.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {
  byte[] getFileBytesByKey(String key);

  void save(MultipartFile file, String fileKey);

  void deleteByFileKeys(List<String> keys);
}
