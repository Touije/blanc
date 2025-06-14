package com.ranblanc.blanc.service;


import com.ranblanc.blanc.dto.ResourceDTO;
import com.ranblanc.blanc.entity.Resource;
import com.ranblanc.blanc.mapper.ResourceMapper;
import com.ranblanc.blanc.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    public ResourceDTO createResource(ResourceDTO resourceDTO) {
        Resource resource = ResourceMapper.toEntity(resourceDTO);
        return ResourceMapper.toDTO(resourceRepository.save(resource));
    }

    public List<ResourceDTO> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(ResourceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ResourceDTO> getResourceById(Long id) {
        return resourceRepository.findById(id).map(ResourceMapper::toDTO);
    }

    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
} 