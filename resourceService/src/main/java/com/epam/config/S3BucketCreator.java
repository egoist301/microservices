package com.epam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;

@Component
public class S3BucketCreator {
  @Value("${cloud.aws.s3.bucket-name}")
  private String bucketName;

  @EventListener(classes = ContextRefreshedEvent.class)
  public void createS3Bucket(ContextRefreshedEvent event) {
    AmazonS3 amazonS3 = event.getApplicationContext().getBean(AmazonS3.class);
    if (!amazonS3.doesBucketExistV2(bucketName)) {
      amazonS3.createBucket(bucketName);
    }
  }
}
