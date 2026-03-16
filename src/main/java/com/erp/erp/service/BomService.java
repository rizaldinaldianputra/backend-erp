package com.erp.erp.service;

import com.erp.erp.dto.manufacturing.BomRequest;
import com.erp.erp.dto.manufacturing.BomResponse;
import com.erp.erp.model.Bom;
import com.erp.erp.model.BomItem;
import com.erp.erp.model.Product;
import com.erp.erp.repository.BomRepository;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.util.CodeGeneratorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BomService {

    private final BomRepository bomRepository;
    private final ProductRepository productRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public BomService(BomRepository bomRepository, ProductRepository productRepository,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.bomRepository = bomRepository;
        this.productRepository = productRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public BomResponse createBom(BomRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Bom bom = new Bom();
        bom.setCode(codeGeneratorUtil.generateCode("BOM"));
        bom.setName(request.getName());
        bom.setProduct(product);
        bom.setQuantity(request.getQuantity());
        bom.setDescription(request.getDescription());
        bom.setStatus(Bom.Status.ACTIVE);

        List<BomItem> items = request.getItems().stream().map(itemReq -> {
            Product material = productRepository.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            BomItem item = new BomItem();
            item.setBom(bom);
            item.setMaterial(material);
            item.setQuantity(itemReq.getQuantity());
            item.setNote(itemReq.getNote());
            return item;
        }).collect(Collectors.toList());

        bom.setItems(items);
        Bom saved = bomRepository.save(bom);
        return mapToResponse(saved);
    }

    public BomResponse updateBom(Long id, BomRequest request) {
        Bom bom = bomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BOM not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        bom.setName(request.getName());
        bom.setProduct(product);
        bom.setQuantity(request.getQuantity());
        bom.setDescription(request.getDescription());

        // Clear existing items (orphanRemoval will delete them from DB)
        bom.getItems().clear();

        // Add new items
        List<BomItem> newItems = request.getItems().stream().map(itemReq -> {
            Product material = productRepository.findById(itemReq.getMaterialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));
            BomItem item = new BomItem();
            item.setBom(bom);
            item.setMaterial(material);
            item.setQuantity(itemReq.getQuantity());
            item.setNote(itemReq.getNote());
            return item;
        }).collect(Collectors.toList());

        bom.getItems().addAll(newItems);

        Bom saved = bomRepository.save(bom);
        return mapToResponse(saved);
    }

    public Page<BomResponse> getAllBoms(Pageable pageable) {
        return bomRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public BomResponse getBomById(Long id) {
        return bomRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("BOM not found"));
    }

    public void deleteBom(Long id) {
        bomRepository.deleteById(id);
    }

    private BomResponse mapToResponse(Bom bom) {
        return BomResponse.builder()
                .id(bom.getId())
                .code(bom.getCode())
                .name(bom.getName())
                .productId(bom.getProduct().getId())
                .productName(bom.getProduct().getName())
                .quantity(bom.getQuantity())
                .description(bom.getDescription())
                .status(bom.getStatus().name())
                .createdDate(bom.getCreatedDate())
                .items(bom.getItems().stream().map(item -> BomResponse.BomItemResponse.builder()
                        .id(item.getId())
                        .materialId(item.getMaterial().getId())
                        .materialName(item.getMaterial().getName())
                        .quantity(item.getQuantity())
                        .note(item.getNote())
                        .build()).collect(Collectors.toList()))
                .build();
    }
}
