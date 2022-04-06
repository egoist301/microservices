package com.epam.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.epam.service.AwsS3Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AwsS3ServiceImpl implements AwsS3Service {
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket-name}")
  private String bucketName;

  @Override
  public byte[] getFileBytesByKey(String key) {
    try {
      return amazonS3.getObject(bucketName, key).getObjectContent().readAllBytes();
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void save(MultipartFile file, String fileKey) {
    try {
      InputStream input = file.getInputStream();
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(input.available());
      final PutObjectRequest putObjectRequest =
          new PutObjectRequest(bucketName, fileKey, input, metadata);
      amazonS3.putObject(putObjectRequest);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void deleteByFileKeys(List<String> keys) {
    DeleteObjectsRequest deleteObjectsRequest =
        new DeleteObjectsRequest(bucketName).withKeys(keys.toArray(String[]::new));
    amazonS3.deleteObjects(deleteObjectsRequest);
  }
}
