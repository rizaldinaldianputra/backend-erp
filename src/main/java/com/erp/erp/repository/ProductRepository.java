package com.erp.erp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.erp.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Cari product berdasarkan code / SKU
    Optional<Product> findByCode(String code);

    // Cari product berdasarkan barcode
    Optional<Product> findByBarcode(String barcode);

    // Cari product berdasarkan QR Code
    Optional<Product> findByQrCode(String qrCode);

    // ===========================
    // Tambahan untuk validasi unik
    // ===========================
    boolean existsByBarcode(String barcode);

    boolean existsByQrCode(String qrCode);
}
