package com.erp.erp.service;

import com.erp.erp.dto.system.ApprovalConfigRequest;
import com.erp.erp.dto.system.ApprovalConfigResponse;
import com.erp.erp.model.ApprovalConfiguration;
import com.erp.erp.repository.ApprovalConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalConfigurationService {

    private final ApprovalConfigurationRepository repository;

    public ApprovalConfigResponse createConfig(ApprovalConfigRequest request) {
        ApprovalConfiguration config = ApprovalConfiguration.builder()
                .documentType(request.getDocumentType())
                .minAmount(request.getMinAmount())
                .maxAmount(request.getMaxAmount())
                .approverRole(request.getApproverRole())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return mapToResponse(repository.save(config));
    }

    public ApprovalConfigResponse updateConfig(Long id, ApprovalConfigRequest request) {
        ApprovalConfiguration config = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));

        config.setDocumentType(request.getDocumentType());
        config.setMinAmount(request.getMinAmount());
        config.setMaxAmount(request.getMaxAmount());
        config.setApproverRole(request.getApproverRole());
        if (request.getActive() != null) {
            config.setActive(request.getActive());
        }

        return mapToResponse(repository.save(config));
    }

    public List<ApprovalConfigResponse> getAllConfigs() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ApprovalConfigResponse> getByDocumentType(String documentType) {
        return repository.findByDocumentTypeAndActiveTrueOrderByMinAmountAsc(documentType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ApprovalConfigResponse getConfigById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
    }

    public void deleteConfig(Long id) {
        repository.deleteById(id);
    }

    private ApprovalConfigResponse mapToResponse(ApprovalConfiguration config) {
        return ApprovalConfigResponse.builder()
                .id(config.getId())
                .documentType(config.getDocumentType())
                .minAmount(config.getMinAmount())
                .maxAmount(config.getMaxAmount())
                .approverRole(config.getApproverRole())
                .active(config.getActive())
                .build();
    }
}
