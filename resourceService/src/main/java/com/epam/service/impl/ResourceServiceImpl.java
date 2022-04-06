package com.epam.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.epam.domain.Resource;
import com.epam.repository.ResourceRepository;
import com.epam.service.AwsS3Service;
import com.epam.service.ResourceCreatedQueueProducerService;
import com.epam.service.ResourceService;
import com.epam.service.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
  private final ResourceRepository resourceRepository;
  private final AwsS3Service awsS3Service;
  private final ResourceCreatedQueueProducerService resourceCreatedQueueProducerService;

  @Transactional
  @Override
  public byte[] findById(Long id) {
    Resource resource =
        resourceRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    return awsS3Service.getFileBytesByKey(resource.getFileKey());
  }

  @Transactional
  @Override
  public Long save(MultipartFile file) {
    Resource resource = new Resource();
    resource.setFileKey(file.getOriginalFilename());
    Resource createdResource = resourceRepository.save(resource);
    awsS3Service.save(file, getFileKey(createdResource.getId(), createdResource.getFileKey()));
    resourceCreatedQueueProducerService.produce(createdResource.getId());
    return createdResource.getId();
  }

  private String getFileKey(Long id, String fileKey) {
    return id + "/" + fileKey;
  }

  @Transactional
  @Override
  public Long[] delete(List<Long> ids) {
    List<Resource> resources = resourceRepository.deleteAllByIdIn(ids);
    awsS3Service.deleteByFileKeys(resources.stream().map(Resource::getFileKey).toList());
    return resources.stream().map(Resource::getId).toArray(Long[]::new);
  }
}
