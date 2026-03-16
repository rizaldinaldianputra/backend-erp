package com.erp.erp.service;

import com.erp.erp.model.Bank;
import com.erp.erp.model.Customer;
import com.erp.erp.model.Tax;
import com.erp.erp.repository.BankRepository;
import com.erp.erp.repository.CustomerRepository;
import com.erp.erp.repository.TaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MasterDataTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private CustomerService customerService;

    @InjectMocks
    private BankService bankService;

    @InjectMocks
    private TaxService taxService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        Customer customer = Customer.builder()
                .code("CUST-001")
                .name("Test Customer")
                .email("test@example.com")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer created = customerService.createCustomer(customer);
        assertNotNull(created);
        assertEquals("CUST-001", created.getCode());
    }

    @Test
    void testCreateBank() {
        Bank bank = Bank.builder()
                .code("BCA")
                .name("Bank Central Asia")
                .build();

        when(bankRepository.save(any(Bank.class))).thenReturn(bank);

        Bank created = bankService.createBank(bank);
        assertNotNull(created);
        assertEquals("BCA", created.getCode());
    }

    @Test
    void testCreateTax() {
        Tax tax = Tax.builder()
                .code("PPN")
                .name("Pajak Pertambahan Nilai")
                .rate(new BigDecimal("11.0"))
                .build();

        when(taxRepository.save(any(Tax.class))).thenReturn(tax);

        Tax created = taxService.createTax(tax);
        assertNotNull(created);
        assertEquals(new BigDecimal("11.0"), created.getRate());
    }
}
