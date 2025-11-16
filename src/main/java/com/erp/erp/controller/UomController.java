package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.UomResponse;
import com.erp.erp.model.Uom;
import com.erp.erp.service.UomService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uom")
@Tag(name = "UOM", description = "Manage UOM (Unit of Measurement)")
public class UomController {

        private final UomService uomService;

        public UomController(UomService uomService) {
                this.uomService = uomService;
        }

        private UomResponse mapToResponse(Uom uom) {
                return UomResponse.builder()
                                .id(uom.getId())
                                .code(uom.getCode())
                                .name(uom.getName())
                                .build();
        }

        @GetMapping
        public ResponseEntity<ApiResponseDto<List<UomResponse>>> getAll() {
                List<UomResponse> list = uomService.getAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();

                return ResponseEntity.ok(
                                ApiResponseDto.<List<UomResponse>>builder()
                                                .status("success")
                                                .message("UOM fetched successfully")
                                                .data(list)
                                                .build());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<UomResponse>> getById(@PathVariable Long id) {
                return uomService.getById(id)
                                .map(uom -> ResponseEntity.ok(
                                                ApiResponseDto.<UomResponse>builder()
                                                                .status("success")
                                                                .message("UOM fetched successfully")
                                                                .data(mapToResponse(uom))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<UomResponse>builder()
                                                                .status("error")
                                                                .message("UOM not found")
                                                                .data(null)
                                                                .build()));
        }

        @PostMapping
        public ResponseEntity<ApiResponseDto<UomResponse>> create(@RequestBody Uom uom) {
                Uom created = uomService.create(uom);
                return ResponseEntity.ok(
                                ApiResponseDto.<UomResponse>builder()
                                                .status("success")
                                                .message("UOM created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<UomResponse>> update(
                        @PathVariable Long id,
                        @RequestBody Uom uom) {

                Uom updated = uomService.update(id, uom);

                return ResponseEntity.ok(
                                ApiResponseDto.<UomResponse>builder()
                                                .status("success")
                                                .message("UOM updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }

}
