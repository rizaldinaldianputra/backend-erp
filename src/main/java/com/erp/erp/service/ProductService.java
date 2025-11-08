package com.erp.erp.service;

import com.erp.erp.model.Product;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.util.QRCodeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final QRCodeUtil qrCodeUtil;

    public ProductService(ProductRepository productRepository, QRCodeUtil qrCodeUtil) {
        this.productRepository = productRepository;
        this.qrCodeUtil = qrCodeUtil;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @SuppressWarnings("null")

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Generate unique barcode
        String uniqueBarcode = "BC" + System.currentTimeMillis();
        product.setBarcode(uniqueBarcode);

        // Generate QR Code Base64 dari barcode
        String qrBase64 = qrCodeUtil.generateQRCodeBase64(uniqueBarcode, 200, 200);
        product.setQrCode(qrBase64);

        return productRepository.save(product);
    }

    @SuppressWarnings("null")

    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setUnit(product.getUnit());
                    existing.setCostPrice(product.getCostPrice());
                    existing.setCategory(product.getCategory());
                    existing.setDescription(product.getDescription());
                    existing.setActive(product.getActive());
                    existing.setImageUrl(product.getImageUrl());

                    // Jika barcode kosong atau berbeda, generate baru
                    if (product.getBarcode() == null || !product.getBarcode().equals(existing.getBarcode())) {
                        String uniqueBarcode = "BC" + System.currentTimeMillis();
                        existing.setBarcode(uniqueBarcode);

                        String qrBase64 = qrCodeUtil.generateQRCodeBase64(uniqueBarcode, 200, 200);
                        existing.setQrCode(qrBase64);
                    }

                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @SuppressWarnings("null")

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
