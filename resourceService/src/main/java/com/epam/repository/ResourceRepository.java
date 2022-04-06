package com.epam.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.domain.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
  List<Resource> deleteAllByIdIn(Collection<Long> ids);
}
