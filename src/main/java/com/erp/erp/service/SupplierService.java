package com.erp.erp.service;

import com.erp.erp.model.Supplier;
import com.erp.erp.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @SuppressWarnings("null")

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        // Generate kode unik supplier
        String uniqueCode = "SUP" + System.currentTimeMillis();
        supplier.setCode(uniqueCode);

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
