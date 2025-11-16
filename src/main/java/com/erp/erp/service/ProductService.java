package com.erp.erp.service;

import com.erp.erp.dto.ProductResponse;
import com.erp.erp.model.Product;
import com.erp.erp.model.Uom;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.repository.UomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UomRepository uomRepository;

    public ProductService(ProductRepository productRepository, UomRepository uomRepository) {
        this.productRepository = productRepository;
        this.uomRepository = uomRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {

        // Validasi UOM
        if (product.getUom() != null && product.getUom().getId() != null) {
            Uom uom = uomRepository.findById(product.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("UOM not found"));
            product.setUom(uom);
        }

        // Generate barcode & QR unik
        product.setBarcode(generateUniqueBarcode());
        product.setQrCode(generateUniqueQrCode());

        // Default active
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
                    existing.setDescription(product.getDescription());
                    existing.setCostPrice(product.getCostPrice());
                    existing.setActive(product.getActive());

                    // UOM update
                    if (product.getUom() != null && product.getUom().getId() != null) {
                        Uom uom = uomRepository.findById(product.getUom().getId())
                                .orElseThrow(() -> new RuntimeException("UOM not found"));
                        existing.setUom(uom);
                    }

                    // Barcode/QR baru bila user sengaja ubah barcode
                    if (product.getBarcode() == null ||
                            !product.getBarcode().equals(existing.getBarcode())) {
                        existing.setBarcode(generateUniqueBarcode());
                        existing.setQrCode(generateUniqueQrCode());
                    }

                    existing.setUpdatedDate(LocalDateTime.now());
                    existing.setUpdatedBy(product.getUpdatedBy());

                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

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

    public ProductResponse toResponse(Product product) {
        if (product == null)
            return null;

        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .build();
    }

}
