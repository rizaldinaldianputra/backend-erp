package com.erp.erp.service;

import com.erp.erp.model.Bank;
import com.erp.erp.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Page<Bank> getAllBanks(Pageable pageable) {
        return bankRepository.findAll(pageable);
    }

    public Optional<Bank> getBankById(Long id) {
        return bankRepository.findById(id);
    }

    @Autowired
    private CodeGeneratorService codeGenerator;

    @Transactional
    public Bank createBank(Bank bank) {
        if (bank.getId() != null) {
            throw new IllegalArgumentException("New bank should not have an ID");
        }
        bank.setCode(codeGenerator.generateSimpleCode("BANK", bankRepository.count() + 1));
        return bankRepository.save(bank);
    }

    @Transactional
    public Bank updateBank(Long id, Bank bankDetails) {
        Bank existing = bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank not found with id: " + id));

        existing.setName(bankDetails.getName());
        existing.setAccountNumber(bankDetails.getAccountNumber());
        existing.setBranch(bankDetails.getBranch());
        existing.setAccountHolder(bankDetails.getAccountHolder());
        existing.setActive(bankDetails.getActive());

        if (bankDetails.getCode() != null && !existing.getCode().equals(bankDetails.getCode())) {
            if (bankRepository.findByCode(bankDetails.getCode()).isPresent()) {
                throw new IllegalArgumentException("Bank code already exists: " + bankDetails.getCode());
            }
            existing.setCode(bankDetails.getCode());
        }

        return bankRepository.save(existing);
    }
}
