package com.erp.erp.service;

import com.erp.erp.model.Product;
import com.erp.erp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Generate unique barcode
        product.setBarcode(generateUniqueBarcode());

        // Generate unique QR code value
        product.setQrCode(generateUniqueQrCode());

        // Set defaults jika belum ada
        if (product.getActive() == null)
            product.setActive(true);
        if (product.getCreatedDate() == null)
            product.setCreatedDate(LocalDateTime.now());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setUnit(product.getUnit());
                    existing.setCostPrice(product.getCostPrice());
                    existing.setDescription(product.getDescription());
                    existing.setActive(product.getActive());

                    // Jika barcode kosong atau berbeda, generate baru
                    if (product.getBarcode() == null || !product.getBarcode().equals(existing.getBarcode())) {
                        existing.setBarcode(generateUniqueBarcode());
                        existing.setQrCode(generateUniqueQrCode());
                    }

                    existing.setUpdatedDate(LocalDateTime.now());
                    existing.setUpdatedBy(product.getUpdatedBy());

                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // ======================
    // Helper methods
    // ======================
    private String generateUniqueBarcode() {
        String barcode;
        do {
            barcode = "BC" + System.currentTimeMillis() + (int) (Math.random() * 1000);
        } while (productRepository.existsByBarcode(barcode));
        return barcode;
    }

    private String generateUniqueQrCode() {
        String qr;
        do {
            qr = UUID.randomUUID().toString();
        } while (productRepository.existsByQrCode(qr));
        return qr;
    }
}
