package com.erp.erp.service;

import com.erp.erp.model.ChartOfAccount;
import com.erp.erp.repository.ChartOfAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ChartOfAccountService {

    private final ChartOfAccountRepository coaRepository;

    public ChartOfAccountService(ChartOfAccountRepository coaRepository) {
        this.coaRepository = coaRepository;
    }

    public Page<ChartOfAccount> getAllAccounts(Pageable pageable) {
        return coaRepository.findAll(pageable);
    }

    public Optional<ChartOfAccount> getAccountById(Long id) {
        return coaRepository.findById(id);
    }

    @Transactional
    public ChartOfAccount createAccount(ChartOfAccount account) {
        if (account.getId() != null) {
            throw new IllegalArgumentException("New account should not have an ID");
        }
        if (account.getCode() != null && coaRepository.findByCode(account.getCode()).isPresent()) {
            throw new IllegalArgumentException("Account code already exists: " + account.getCode());
        }

        // Validate Parent
        if (account.getParent() != null && account.getParent().getId() != null) {
            ChartOfAccount parent = coaRepository.findById(account.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Parent account not found"));
            account.setParent(parent);
        }

        return coaRepository.save(account);
    }

    @Transactional
    public ChartOfAccount updateAccount(Long id, ChartOfAccount accountDetails) {
        ChartOfAccount existing = coaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        existing.setName(accountDetails.getName());
        existing.setType(accountDetails.getType());
        existing.setLevel(accountDetails.getLevel());
        existing.setActive(accountDetails.getActive());

        if (accountDetails.getCode() != null && !existing.getCode().equals(accountDetails.getCode())) {
            if (coaRepository.findByCode(accountDetails.getCode()).isPresent()) {
                throw new IllegalArgumentException("Account code already exists: " + accountDetails.getCode());
            }
            existing.setCode(accountDetails.getCode());
        }

        // Validate Parent
        if (accountDetails.getParent() != null && accountDetails.getParent().getId() != null) {
            ChartOfAccount parent = coaRepository.findById(accountDetails.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Parent account not found"));
            // Prevent circular reference: check if parent is self or child of self
            // (simplified check)
            if (parent.getId().equals(existing.getId())) {
                throw new IllegalArgumentException("Account cannot be its own parent");
            }
            existing.setParent(parent);
        } else {
            existing.setParent(null);
        }

        return coaRepository.save(existing);
    }
}
