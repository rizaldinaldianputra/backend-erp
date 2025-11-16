package com.erp.erp.service;

import com.erp.erp.model.*;
import com.erp.erp.repository.PurchaseOrderRepository;
import com.erp.erp.repository.PurchaseRequestRepository;
import com.erp.erp.repository.SupplierRepository;
import com.erp.erp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository poRepository;
    private final PurchaseRequestRepository prRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public PurchaseOrderService(
            PurchaseOrderRepository poRepository,
            PurchaseRequestRepository prRepository,
            SupplierRepository supplierRepository,
            UserRepository userRepository) {
        this.poRepository = poRepository;
        this.prRepository = prRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    public List<PurchaseOrder> getAll() {
        return poRepository.findAll();
    }

    public Optional<PurchaseOrder> getById(Long id) {
        return poRepository.findById(id);
    }

    public PurchaseOrder create(PurchaseOrder po) {

        // Generate nomor dokumen PO/173343...
        if (po.getDocumentNumber() == null || po.getDocumentNumber().isEmpty()) {
            po.setDocumentNumber("PO/" + System.currentTimeMillis());
        }

        // Set tanggal transaksi jika tidak ada
        if (po.getTrxDate() == null) {
            po.setTrxDate(LocalDateTime.now());
        }

        // Relasi PR (opsional)
        if (po.getPurchaseRequest() != null && po.getPurchaseRequest().getId() != null) {
            var pr = prRepository.findById(po.getPurchaseRequest().getId())
                    .orElseThrow(() -> new RuntimeException("Purchase Request not found"));
            po.setPurchaseRequest(pr);
        }

        // Supplier (opsional)
        if (po.getSupplier() != null && po.getSupplier().getId() != null) {
            var supplier = supplierRepository.findById(po.getSupplier().getId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found"));
            po.setSupplier(supplier);
        }

        // createdByUser
        if (po.getCreatedByUser() != null && po.getCreatedByUser().getId() != null) {
            var createdBy = userRepository.findById(po.getCreatedByUser().getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            po.setCreatedByUser(createdBy);
        }

        // set parent pada tiap items
        if (po.getItems() != null) {
            po.getItems().forEach(i -> i.setPurchaseOrder(po));
        }

        return poRepository.save(po);
    }

    public PurchaseOrder update(Long id, PurchaseOrder po) {
        return poRepository.findById(id)
                .map(existing -> {

                    existing.setNotes(po.getNotes());
                    existing.setStatus(po.getStatus());
                    existing.setTrxDate(po.getTrxDate());

                    // Supplier update
                    if (po.getSupplier() != null && po.getSupplier().getId() != null) {
                        var supplier = supplierRepository.findById(po.getSupplier().getId())
                                .orElseThrow(() -> new RuntimeException("Supplier not found"));
                        existing.setSupplier(supplier);
                    }

                    // updatedByUser
                    if (po.getUpdatedByUser() != null && po.getUpdatedByUser().getId() != null) {
                        var updatedBy = userRepository.findById(po.getUpdatedByUser().getId())
                                .orElseThrow(() -> new RuntimeException("User not found"));
                        existing.setUpdatedByUser(updatedBy);
                    }

                    // Items update
                    existing.getItems().clear();
                    if (po.getItems() != null) {
                        po.getItems().forEach(i -> {
                            i.setPurchaseOrder(existing);
                            existing.getItems().add(i);
                        });
                    }

                    existing.setUpdatedDate(LocalDateTime.now());
                    return poRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Purchase Order not found"));
    }

    public void delete(Long id) {
        poRepository.deleteById(id);
    }
}
