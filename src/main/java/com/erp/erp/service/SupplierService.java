package com.erp.erp.service;

import com.erp.erp.model.Supplier;
import com.erp.erp.repository.SupplierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Page<Supplier> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    @SuppressWarnings("null")

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    @org.springframework.beans.factory.annotation.Autowired
    private CodeGeneratorService codeGenerator;

    public Supplier createSupplier(Supplier supplier) {
        long count = supplierRepository.count() + 1;
        supplier.setCode(codeGenerator.generateSimpleCode("SUP", count));
        return supplierRepository.save(supplier);
    }

    @SuppressWarnings("null")
    public Supplier updateSupplier(Long id, Supplier supplier) {
        return supplierRepository.findById(id)
                .map(existing -> {
                    existing.setName(supplier.getName());
                    existing.setEmail(supplier.getEmail());
                    existing.setPhone(supplier.getPhone());
                    existing.setAddress(supplier.getAddress());
                    existing.setWebsite(supplier.getWebsite());
                    existing.setNpwp(supplier.getNpwp());
                    existing.setActive(supplier.getActive());
                    return supplierRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @SuppressWarnings("null")

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
