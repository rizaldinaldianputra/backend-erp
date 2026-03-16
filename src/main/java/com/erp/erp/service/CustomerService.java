package com.erp.erp.service;

import com.erp.erp.model.Customer;
import com.erp.erp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Autowired
    private CodeGeneratorService codeGenerator;

    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getId() != null) {
            throw new IllegalArgumentException("New customer should not have an ID");
        }
        long count = customerRepository.count() + 1;
        customer.setCode(codeGenerator.generateSimpleCode("CUST", count));
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existing.setName(customerDetails.getName());
        existing.setEmail(customerDetails.getEmail());
        existing.setPhone(customerDetails.getPhone());
        existing.setAddress(customerDetails.getAddress());
        existing.setNpwp(customerDetails.getNpwp());
        existing.setCreditLimit(customerDetails.getCreditLimit());
        existing.setActive(customerDetails.getActive());

        // Code update usually restricted, but if needed, check uniqueness
        if (customerDetails.getCode() != null && !existing.getCode().equals(customerDetails.getCode())) {
            if (customerRepository.findByCode(customerDetails.getCode()).isPresent()) {
                throw new IllegalArgumentException("Customer code already exists: " + customerDetails.getCode());
            }
            existing.setCode(customerDetails.getCode());
        }

        return customerRepository.save(existing);
    }
}
