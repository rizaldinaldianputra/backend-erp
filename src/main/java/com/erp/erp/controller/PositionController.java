package com.erp.erp.controller;

import com.erp.erp.dto.ApiResponseDto;
import com.erp.erp.dto.PositionResponse;
import com.erp.erp.model.Position;
import com.erp.erp.service.CategoryService;
import com.erp.erp.service.PositionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/positions")
@Tag(name = "Position", description = "Manage job positions")
public class PositionController {

        private final PositionService positionService;

        public PositionController(PositionService service) {
                this.positionService = service;
        }

        private PositionResponse mapToResponse(Position p) {
                return PositionResponse.builder()
                                .id(p.getId())
                                .code(p.getCode())
                                .title(p.getTitle())
                                .department(p.getDepartment())
                                .build();
        }

        @GetMapping
        public ResponseEntity<ApiResponseDto<List<PositionResponse>>> getAll() {
                List<PositionResponse> list = positionService.getAllPositions()
                                .stream().map(this::mapToResponse).collect(Collectors.toList());

                return ResponseEntity.ok(
                                ApiResponseDto.<List<PositionResponse>>builder()
                                                .status("success")
                                                .message("Positions fetched successfully")
                                                .data(list)
                                                .build());
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponseDto<PositionResponse>> getById(@PathVariable Long id) {
                return positionService.getPositionById(id)
                                .map(p -> ResponseEntity.ok(
                                                ApiResponseDto.<PositionResponse>builder()
                                                                .status("success")
                                                                .message("Position fetched successfully")
                                                                .data(mapToResponse(p))
                                                                .build()))
                                .orElse(ResponseEntity.status(404).body(
                                                ApiResponseDto.<PositionResponse>builder()
                                                                .status("error")
                                                                .message("Position not found")
                                                                .data(null)
                                                                .build()));
        }

        @PostMapping
        public ResponseEntity<ApiResponseDto<PositionResponse>> create(@RequestBody Position pos) {
                Position created = positionService.createPosition(pos);

                return ResponseEntity.ok(
                                ApiResponseDto.<PositionResponse>builder()
                                                .status("success")
                                                .message("Position created successfully")
                                                .data(mapToResponse(created))
                                                .build());
        }

        @PutMapping("/{id}")
        public ResponseEntity<ApiResponseDto<PositionResponse>> update(
                        @PathVariable Long id, @RequestBody Position pos) {

                Position updated = positionService.updatePosition(id, pos);

                return ResponseEntity.ok(
                                ApiResponseDto.<PositionResponse>builder()
                                                .status("success")
                                                .message("Position updated successfully")
                                                .data(mapToResponse(updated))
                                                .build());
        }
}
