package com.erp.erp.service;

import com.erp.erp.dto.OfficeResponse;
import com.erp.erp.model.Office;
import com.erp.erp.model.Organization;
import com.erp.erp.repository.OfficeRepository;
import com.erp.erp.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final OrganizationRepository organizationRepository;

    public OfficeService(OfficeRepository officeRepository, OrganizationRepository organizationRepository) {
        this.officeRepository = officeRepository;
        this.organizationRepository = organizationRepository;
    }

    // GET all offices
    public List<OfficeResponse> findAllWithOrganization() {
        return officeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET office by ID
    public Optional<OfficeResponse> findById(Long id) {
        return officeRepository.findById(id)
                .map(this::mapToResponse);
    }

    // GET offices by organization ID
    public List<OfficeResponse> findByOrganizationId(Long orgId) {
        return officeRepository.findByOrganizationId(orgId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // CREATE
    public Office create(Office office) {
        if (office.getOrganization() == null || office.getOrganization().getId() == null) {
            throw new RuntimeException("Organization is required");
        }
        Organization org = organizationRepository.findById(office.getOrganization().getId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        office.setOrganization(org);
        return officeRepository.save(office);
    }

    // UPDATE
    public OfficeResponse update(Long id, Office office) {
        Office existing = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Office not found with id " + id));

        existing.setName(office.getName());
        existing.setAddress(office.getAddress());
        existing.setCode(office.getCode());
        existing.setPhone(office.getPhone());
        existing.setEmail(office.getEmail());
        existing.setWebsite(office.getWebsite());

        if (office.getOrganization() != null && office.getOrganization().getId() != null) {
            Organization org = organizationRepository.findById(office.getOrganization().getId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            existing.setOrganization(org);
        }

        Office updated = officeRepository.save(existing);
        return mapToResponse(updated);
    }

    // DELETE
    public void delete(Long id) {
        officeRepository.deleteById(id);
    }

    // Mapping entity -> DTO
    public OfficeResponse mapToResponse(Office office) {
        return OfficeResponse.builder()
                .id(office.getId())
                .name(office.getName())
                .address(office.getAddress())
                .code(office.getCode())
                .phone(office.getPhone())
                .email(office.getEmail())
                .website(office.getWebsite())
                .organization(office.getOrganization())
                .build();
    }
}
