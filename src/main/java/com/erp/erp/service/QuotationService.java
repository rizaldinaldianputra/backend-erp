package com.erp.erp.service;

import com.erp.erp.dto.sales.QuotationRequest;
import com.erp.erp.model.Customer;
import com.erp.erp.model.Product;
import com.erp.erp.model.Quotation;
import com.erp.erp.model.QuotationItem;
import com.erp.erp.repository.CustomerRepository;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.repository.QuotationRepository;
import com.erp.erp.util.CodeGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public QuotationService(
            QuotationRepository quotationRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.quotationRepository = quotationRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public Quotation createQuotation(QuotationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Quotation quotation = new Quotation();
        quotation.setCode(codeGeneratorUtil.generateCode("QT"));
        quotation.setCustomer(customer);
        quotation.setDate(request.getDate());
        quotation.setValidUntil(request.getValidUntil());
        quotation.setNote(request.getNote());
        quotation.setStatus(Quotation.Status.DRAFT);

        // Calculate totals
        BigDecimal subTotal = BigDecimal.ZERO;
        List<QuotationItem> items = new ArrayList<>();

        for (QuotationRequest.QuotationItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            QuotationItem item = new QuotationItem();
            item.setQuotation(quotation);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());

            BigDecimal price = product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO;
            item.setPrice(price);

            item.setDiscount(itemRequest.getDiscount() != null ? itemRequest.getDiscount() : BigDecimal.ZERO);

            BigDecimal lineTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()))
                    .subtract(item.getDiscount());
            item.setTotal(lineTotal);

            items.add(item);
            subTotal = subTotal.add(lineTotal);
        }

        quotation.setItems(items);
        quotation.setSubTotal(subTotal);
        quotation.setTaxAmount(BigDecimal.ZERO);
        quotation.setDiscountAmount(BigDecimal.ZERO);
        quotation.setTotalAmount(subTotal);

        return quotationRepository.save(quotation);
    }

    public Quotation updateQuotation(Long id, QuotationRequest request) {
        Quotation quotation = getQuotationById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            quotation.setCustomer(customer);
        }
        if (request.getDate() != null)
            quotation.setDate(request.getDate());
        if (request.getValidUntil() != null)
            quotation.setValidUntil(request.getValidUntil());
        if (request.getNote() != null)
            quotation.setNote(request.getNote());

        return quotationRepository.save(quotation);
    }

    public List<Quotation> getAllQuotations() {
        return quotationRepository.findAll();
    }

    public Optional<Quotation> getQuotationById(Long id) {
        return quotationRepository.findById(id);
    }

    public void deleteQuotation(Long id) {
        quotationRepository.deleteById(id);
    }

    public Quotation updateStatus(Long id, Quotation.Status status) {
        Quotation quotation = getQuotationById(id)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));
        quotation.setStatus(status);
        return quotationRepository.save(quotation);
    }
}
