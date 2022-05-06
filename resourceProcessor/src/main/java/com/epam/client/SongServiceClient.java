package com.epam.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;

import com.epam.model.SongMetadata;

@FeignClient("songservice")
public interface SongServiceClient {
  @Retryable(
      value = RuntimeException.class,
      maxAttemptsExpression = "${song-service.request.retries-count}",
      backoff =
          @Backoff(
              delayExpression = "${song-service.request.base-retry-delay}",
              maxDelayExpression = "${song-service.request.max-retry-delay}",
              multiplierExpression = "${song-service.request.retry-delay-multiplier}"))
  @PostMapping("/songs")
  Map<String, Long> saveSong(SongMetadata songMetadata);
}
