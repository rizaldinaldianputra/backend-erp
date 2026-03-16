package com.erp.erp.service;

import com.erp.erp.dto.finance.FixedAssetRequest;
import com.erp.erp.dto.finance.FixedAssetResponse;
import com.erp.erp.model.ChartOfAccount;
import com.erp.erp.model.FixedAsset;
import com.erp.erp.repository.ChartOfAccountRepository;
import com.erp.erp.repository.FixedAssetRepository;
import com.erp.erp.util.CodeGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FixedAssetService {

        private final FixedAssetRepository fixedAssetRepository;
        private final ChartOfAccountRepository chartOfAccountRepository;
        private final CodeGeneratorUtil codeGeneratorUtil;

        @Transactional
        public FixedAssetResponse createFixedAsset(FixedAssetRequest request) {
                ChartOfAccount coa = request.getChartOfAccountId() != null
                                ? chartOfAccountRepository.findById(request.getChartOfAccountId()).orElse(null)
                                : null;

                String code = codeGeneratorUtil.generateCode("FA");

                FixedAsset fixedAsset = FixedAsset.builder()
                                .code(code)
                                .name(request.getName())
                                .description(request.getDescription())
                                .chartOfAccount(coa)
                                .purchaseDate(request.getPurchaseDate())
                                .purchasePrice(request.getPurchasePrice())
                                .salvageValue(request.getSalvageValue())
                                .usefulLifeYears(request.getUsefulLifeYears())
                                .depreciationMethod(request.getDepreciationMethod())
                                .status(FixedAsset.Status.ACTIVE)
                                .build();

                FixedAsset savedAsset = fixedAssetRepository.save(fixedAsset);
                return mapToResponse(savedAsset);
        }

        @Transactional(readOnly = true)
        public Page<FixedAssetResponse> getAllFixedAssets(Pageable pageable) {
                return fixedAssetRepository.findAll(pageable)
                                .map(this::mapToResponse);
        }

        @Transactional(readOnly = true)
        public FixedAssetResponse getFixedAssetById(Long id) {
                FixedAsset fixedAsset = fixedAssetRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Fixed Asset not found"));
                return mapToResponse(fixedAsset);
        }

        @Transactional
        public FixedAssetResponse disposeFixedAsset(Long id) {
                FixedAsset fixedAsset = fixedAssetRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Fixed Asset not found"));

                fixedAsset.setStatus(FixedAsset.Status.DISPOSED);
                FixedAsset updatedAsset = fixedAssetRepository.save(fixedAsset);
                return mapToResponse(updatedAsset);
        }

        private FixedAssetResponse mapToResponse(FixedAsset fixedAsset) {
                return FixedAssetResponse.builder()
                                .id(fixedAsset.getId())
                                .code(fixedAsset.getCode())
                                .name(fixedAsset.getName())
                                .description(fixedAsset.getDescription())
                                .chartOfAccountId(
                                                fixedAsset.getChartOfAccount() != null
                                                                ? fixedAsset.getChartOfAccount().getId()
                                                                : null)
                                .chartOfAccountName(
                                                fixedAsset.getChartOfAccount() != null
                                                                ? fixedAsset.getChartOfAccount().getName()
                                                                : null)
                                .purchaseDate(fixedAsset.getPurchaseDate())
                                .purchasePrice(fixedAsset.getPurchasePrice())
                                .salvageValue(fixedAsset.getSalvageValue())
                                .usefulLifeYears(fixedAsset.getUsefulLifeYears())
                                .depreciationMethod(fixedAsset.getDepreciationMethod().name())
                                .status(fixedAsset.getStatus().name())
                                .depreciationHistories(fixedAsset.getDepreciationHistories().stream()
                                                .map(history -> FixedAssetResponse.DepreciationHistoryResponse.builder()
                                                                .id(history.getId())
                                                                .depreciationDate(history.getDepreciationDate())
                                                                .amount(history.getAmount())
                                                                .accumulatedDepreciation(
                                                                                history.getAccumulatedDepreciation())
                                                                .bookValue(history.getBookValue())
                                                                .note(history.getNote())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();
        }
}
