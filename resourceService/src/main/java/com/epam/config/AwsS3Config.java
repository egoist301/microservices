package com.epam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@EnableAutoConfiguration(exclude = ContextInstanceDataAutoConfiguration.class)
public class AwsS3Config {
  @Value("${cloud.aws.s3.url}")
  private String url;

  @Value("${cloud.aws.s3.access-key}")
  private String accessKey;

  @Value("${cloud.aws.s3.secret-key}")
  private String secretKey;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Primary
  @Bean
  public AmazonS3 amazonS3() {
    return AmazonS3Client.builder()
        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(url, region))
        .withCredentials(
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
        .withPathStyleAccessEnabled(false)
        .build();
  }
}
