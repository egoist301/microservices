package com.epam.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "resource-service", url = "${resource-service.url}")
public interface ResourceServiceClient {
  @Retryable(
      value = RuntimeException.class,
      maxAttemptsExpression = "${resource-service.request.retries-count}",
      backoff =
          @Backoff(
              delayExpression = "${resource-service.request.base-retry-delay}",
              maxDelayExpression = "${resource-service.request.max-retry-delay}",
              multiplierExpression = "${resource-service.request.retry-delay-multiplier}"))
  @GetMapping("/resources/{resourceId}")
  byte[] getResourceBinaryData(@PathVariable Long resourceId);
}
