package com.erp.erp.service;

import com.erp.erp.dto.WarehouseResponse;
import com.erp.erp.model.Organization;
import com.erp.erp.model.Warehouse;
import com.erp.erp.repository.OrganizationRepository;
import com.erp.erp.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final OrganizationRepository organizationRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, OrganizationRepository organizationRepository) {
        this.warehouseRepository = warehouseRepository;
        this.organizationRepository = organizationRepository;
    }

    // GET all warehouses
    public List<WarehouseResponse> findAll() {
        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET warehouse by ID
    public Optional<WarehouseResponse> findById(Long id) {
        return warehouseRepository.findById(id)
                .map(this::mapToResponse);
    }

    // CREATE new warehouse
    public WarehouseResponse create(Warehouse warehouse) {
        if (warehouse.getOrganization() == null || warehouse.getOrganization().getId() == null) {
            throw new RuntimeException("Organization is required");
        }
        Organization org = organizationRepository.findById(warehouse.getOrganization().getId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        warehouse.setOrganization(org);
        Warehouse saved = warehouseRepository.save(warehouse);
        return mapToResponse(saved);
    }

    // UPDATE warehouse
    public WarehouseResponse update(Long id, Warehouse warehouse) {
        Warehouse existing = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        existing.setName(warehouse.getName());
        existing.setAddress(warehouse.getAddress());
        existing.setCode(warehouse.getCode());

        if (warehouse.getOrganization() != null && warehouse.getOrganization().getId() != null) {
            Organization org = organizationRepository.findById(warehouse.getOrganization().getId())
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            existing.setOrganization(org);
        }

        Warehouse updated = warehouseRepository.save(existing);
        return mapToResponse(updated);
    }

    // DELETE warehouse
    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }

    // Mapping entity -> DTO
    public WarehouseResponse mapToResponse(Warehouse warehouse) {
        return WarehouseResponse.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .address(warehouse.getAddress())
                .code(warehouse.getCode())
                .organization(warehouse.getOrganization()) // menampilkan object Organization lengkap
                .build();
    }

    // GET warehouses by Organization ID
    public List<WarehouseResponse> findByOrganization(Long organizationId) {
        return warehouseRepository.findByOrganizationId(organizationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
