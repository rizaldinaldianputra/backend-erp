package com.erp.erp.service;

import com.erp.erp.model.Uom;
import com.erp.erp.repository.UomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UomService {

    private final UomRepository uomRepository;

    public UomService(UomRepository uomRepository) {
        this.uomRepository = uomRepository;
    }

    public List<Uom> getAll() {
        return uomRepository.findAll();
    }

    public Optional<Uom> getById(Long id) {
        return uomRepository.findById(id);
    }

    public Uom create(Uom uom) {
        return uomRepository.save(uom);
    }

    public Uom update(Long id, Uom newData) {
        return uomRepository.findById(id)
                .map(existing -> {
                    existing.setCode(newData.getCode());
                    existing.setName(newData.getName());
                    return uomRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Uom not found"));
    }

    public void delete(Long id) {
        uomRepository.deleteById(id);
    }
}
