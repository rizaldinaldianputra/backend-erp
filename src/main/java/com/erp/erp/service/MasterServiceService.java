package com.erp.erp.service;

import com.erp.erp.dto.MasterServiceResponse;
import com.erp.erp.model.MasterService;
import com.erp.erp.repository.MasterServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MasterServiceService {

    private final MasterServiceRepository masterServiceRepository;

    public MasterServiceService(MasterServiceRepository masterServiceRepository) {
        this.masterServiceRepository = masterServiceRepository;
    }

    public List<MasterServiceResponse> findAll() {
        return masterServiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Optional<MasterServiceResponse> findById(Long id) {
        return masterServiceRepository.findById(id)
                .map(this::mapToResponse);
    }

    public MasterService create(MasterService service) {
        return masterServiceRepository.save(service);
    }

    public MasterServiceResponse update(Long id, MasterService service) {
        MasterService existing = masterServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        existing.setName(service.getName());
        existing.setDescription(service.getDescription());
        existing.setUnitPrice(service.getUnitPrice());
        existing.setActive(service.getActive());
        MasterService updated = masterServiceRepository.save(existing);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        masterServiceRepository.deleteById(id);
    }

    public MasterServiceResponse mapToResponse(MasterService service) {
        return MasterServiceResponse.builder()
                .id(service.getId())
                .code(service.getCode())
                .name(service.getName())
                .description(service.getDescription())
                .unitPrice(service.getUnitPrice())
                .active(service.getActive())
                .build();
    }
}
