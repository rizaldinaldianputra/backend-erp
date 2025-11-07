package com.erp.erp.service.product.impl;

import com.erp.erp.model.Product;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.service.product.ProductService;
import com.erp.erp.util.QRCodeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final QRCodeUtil qrCodeUtil;

    // Constructor injection untuk ProductRepository dan QRCodeUtil
    public ProductServiceImpl(ProductRepository productRepository, QRCodeUtil qrCodeUtil) {
        this.productRepository = productRepository;
        this.qrCodeUtil = qrCodeUtil;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        // Generate unique barcode
        String uniqueBarcode = "BC" + System.currentTimeMillis();
        product.setBarcode(uniqueBarcode);

        // Generate QR Code Base64 dari barcode
        String qrBase64 = qrCodeUtil.generateQRCodeBase64(uniqueBarcode, 200, 200);
        product.setQrCode(qrBase64);

        return productRepository.save(product);
    }

    @Override
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

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
