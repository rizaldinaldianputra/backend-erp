package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.purchase.GoodsReceiptRequest;
import com.erp.erp.model.GoodsReceipt;
import com.erp.erp.service.GoodsReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods-receipts")
@Tag(name = "Purchasing - Goods Receipt", description = "Manage Goods Receipts")
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;
    private final com.erp.erp.service.ReportService reportService;

    public GoodsReceiptController(GoodsReceiptService goodsReceiptService,
            com.erp.erp.service.ReportService reportService) {
        this.goodsReceiptService = goodsReceiptService;
        this.reportService = reportService;
    }

    @PostMapping
    @Operation(summary = "Create Goods Receipt (Updates Inventory)")
    public ResponseEntity<ApiResponseDto<GoodsReceipt>> createGoodsReceipt(@RequestBody GoodsReceiptRequest request) {
        GoodsReceipt gr = goodsReceiptService.createGoodsReceipt(request);
        return ResponseEntity.ok(ApiResponseDto.<GoodsReceipt>builder()
                .status("success")
                .data(gr)
                .build());
    }

    @GetMapping
    @Operation(summary = "Get all Goods Receipts")
    public ResponseEntity<ApiResponseDto<List<GoodsReceipt>>> getAllGoodsReceipts() {
        return ResponseEntity.ok(ApiResponseDto.<List<GoodsReceipt>>builder()
                .status("success")
                .data(goodsReceiptService.getAllGoodsReceipts())
                .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Goods Receipt by ID")
    public ResponseEntity<ApiResponseDto<GoodsReceipt>> getGoodsReceiptById(@PathVariable Long id) {
        return goodsReceiptService.getGoodsReceiptById(id)
                .map(gr -> ResponseEntity.ok(ApiResponseDto.<GoodsReceipt>builder()
                        .status("success")
                        .data(gr)
                        .build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/report")
    @Operation(summary = "Download Goods Receipt Report")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        GoodsReceipt gr = goodsReceiptService.getGoodsReceiptById(id)
                .orElseThrow(() -> new RuntimeException("Goods Receipt not found"));

        try {
            java.util.Map<String, Object> parameters = new java.util.HashMap<>();
            parameters.put("receiptCode", gr.getCode() != null ? gr.getCode() : "");
            parameters.put("purchaseOrderCode",
                    gr.getPurchaseOrder() != null && gr.getPurchaseOrder().getDocumentNumber() != null
                            ? gr.getPurchaseOrder().getDocumentNumber()
                            : "");
            parameters.put("receiptDate", gr.getDate() != null ? gr.getDate().toString() : "");
            parameters.put("receivedBy", gr.getCreatedBy() != null ? gr.getCreatedBy() : "");
            parameters.put("note", gr.getNote() != null ? gr.getNote() : "");

            List<java.util.Map<String, Object>> itemsData = new java.util.ArrayList<>();
            if (gr.getItems() != null) {
                for (var item : gr.getItems()) {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("productName", item.getProduct() != null ? item.getProduct().getName() : "");
                    map.put("quantity", item.getQuantity() != null ? item.getQuantity() : 0);
                    itemsData.add(map);
                }
            }

            byte[] pdfBytes = reportService.generatePdfReport("reports/goods_receipt_detail_report.jrxml", parameters,
                    itemsData);

            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "GR_" + gr.getCode() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
