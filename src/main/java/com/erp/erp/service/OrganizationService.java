package com.erp.erp.service;

import com.erp.erp.dto.OrganizationResponse;
import com.erp.erp.model.Organization;
import com.erp.erp.repository.OrganizationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    // GET all organizations with pagination
    public Page<OrganizationResponse> findAll(Pageable pageable) {
        return organizationRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // GET organization by ID
    public Optional<OrganizationResponse> findById(Long id) {
        return organizationRepository.findById(id)
                .map(this::toResponse);
    }

    // CREATE new organization
    public OrganizationResponse create(Organization org) {
        if (organizationRepository.existsByCode(org.getCode())) {
            throw new RuntimeException("Organization code already exists: " + org.getCode());
        }
        Organization saved = organizationRepository.save(org);
        return toResponse(saved);
    }

    // UPDATE organization
    public OrganizationResponse update(Long id, Organization org) {
        Organization existing = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id " + id));

        existing.setName(org.getName());
        existing.setCode(org.getCode());
        existing.setAddress(org.getAddress());
        existing.setPhone(org.getPhone());
        existing.setEmail(org.getEmail());
        existing.setWebsite(org.getWebsite());
        existing.setLogoUrl(org.getLogoUrl());
        existing.setUpdatedBy(org.getUpdatedBy());
        existing.setActive(org.getActive()); // pastikan ada field active di entity

        Organization updated = organizationRepository.save(existing);
        return toResponse(updated);
    }

    // DELETE organization
    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }

    // Mapping entity -> DTO response
    private OrganizationResponse toResponse(Organization org) {
        return OrganizationResponse.builder()
                .id(org.getId())
                .code(org.getCode())
                .name(org.getName())
                .address(org.getAddress())
                .phone(org.getPhone())
                .email(org.getEmail())
                .active(org.getActive()) // pastikan field active ada di entity
                .build();
    }
}
