package com.erp.erp.service;

import com.erp.erp.model.Tax;
import com.erp.erp.repository.TaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaxService {

    private final TaxRepository taxRepository;

    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    public Page<Tax> getAllTaxes(Pageable pageable) {
        return taxRepository.findAll(pageable);
    }

    public Optional<Tax> getTaxById(Long id) {
        return taxRepository.findById(id);
    }

    @Autowired
    private CodeGeneratorService codeGenerator;

    @Transactional
    public Tax createTax(Tax tax) {
        if (tax.getId() != null) {
            throw new IllegalArgumentException("New tax should not have an ID");
        }
        tax.setCode(codeGenerator.generateSimpleCode("TAX", taxRepository.count() + 1));
        return taxRepository.save(tax);
    }

    @Transactional
    public Tax updateTax(Long id, Tax taxDetails) {
        Tax existing = taxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax not found with id: " + id));

        existing.setName(taxDetails.getName());
        existing.setRate(taxDetails.getRate());
        existing.setActive(taxDetails.getActive());

        if (taxDetails.getCode() != null && !existing.getCode().equals(taxDetails.getCode())) {
            if (taxRepository.findByCode(taxDetails.getCode()).isPresent()) {
                throw new IllegalArgumentException("Tax code already exists: " + taxDetails.getCode());
            }
            existing.setCode(taxDetails.getCode());
        }

        return taxRepository.save(existing);
    }
}
