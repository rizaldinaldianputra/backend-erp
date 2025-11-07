package com.erp.erp.repository;

import com.erp.erp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Cari product berdasarkan code / SKU
    Optional<Product> findByCode(String code);

    // Cari product berdasarkan barcode
    Optional<Product> findByBarcode(String barcode);

    // Cari product berdasarkan QR Code
    Optional<Product> findByQrCode(String qrCode);
}
