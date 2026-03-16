package com.erp.erp.service;

import com.erp.erp.dto.sales.SalesOrderRequest;
import com.erp.erp.model.*;
import com.erp.erp.repository.CustomerRepository;
import com.erp.erp.repository.ProductRepository;
import com.erp.erp.repository.QuotationRepository;
import com.erp.erp.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.erp.erp.util.CodeGeneratorUtil;

@Service
@Transactional
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final QuotationRepository quotationRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CodeGeneratorUtil codeGeneratorUtil;

    public SalesOrderService(
            SalesOrderRepository salesOrderRepository,
            QuotationRepository quotationRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            CodeGeneratorUtil codeGeneratorUtil) {
        this.salesOrderRepository = salesOrderRepository;
        this.quotationRepository = quotationRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.codeGeneratorUtil = codeGeneratorUtil;
    }

    public SalesOrder createSalesOrder(SalesOrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCode("SO-" + System.currentTimeMillis());
        salesOrder.setCustomer(customer);
        salesOrder.setDate(request.getDate());
        salesOrder.setDeliveryDate(request.getDeliveryDate());
        salesOrder.setNote(request.getNote());
        salesOrder.setStatus(SalesOrder.Status.DRAFT);

        if (request.getQuotationId() != null) {
            Quotation quotation = quotationRepository.findById(request.getQuotationId())
                    .orElseThrow(() -> new RuntimeException("Quotation not found"));
            salesOrder.setQuotation(quotation);
        }

        BigDecimal subTotal = BigDecimal.ZERO;
        List<SalesOrderItem> items = new ArrayList<>();

        for (SalesOrderRequest.SalesOrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            SalesOrderItem item = new SalesOrderItem();
            item.setSalesOrder(salesOrder);
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

        salesOrder.setItems(items);
        salesOrder.setSubTotal(subTotal);
        salesOrder.setTaxAmount(BigDecimal.ZERO);
        salesOrder.setDiscountAmount(BigDecimal.ZERO);
        salesOrder.setTotalAmount(subTotal);

        return salesOrderRepository.save(salesOrder);
    }

    public SalesOrder createSalesOrderFromQuotation(Long quotationId) {
        Quotation quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCode(codeGeneratorUtil.generateCode("SO"));
        salesOrder.setCustomer(quotation.getCustomer());
        salesOrder.setQuotation(quotation);
        salesOrder.setDate(LocalDate.now());
        salesOrder.setNote("Created from Quotation " + quotation.getCode());
        salesOrder.setStatus(SalesOrder.Status.DRAFT);

        BigDecimal subTotal = BigDecimal.ZERO;
        List<SalesOrderItem> items = new ArrayList<>();

        for (QuotationItem qItem : quotation.getItems()) {
            SalesOrderItem item = new SalesOrderItem();
            item.setSalesOrder(salesOrder);
            item.setProduct(qItem.getProduct());
            item.setQuantity(qItem.getQuantity());
            item.setPrice(qItem.getPrice());
            item.setDiscount(qItem.getDiscount());
            item.setTotal(qItem.getTotal());

            items.add(item);
            subTotal = subTotal.add(qItem.getTotal());
        }

        salesOrder.setItems(items);
        salesOrder.setSubTotal(quotation.getSubTotal());
        salesOrder.setTaxAmount(quotation.getTaxAmount());
        salesOrder.setDiscountAmount(quotation.getDiscountAmount());
        salesOrder.setTotalAmount(quotation.getTotalAmount());

        return salesOrderRepository.save(salesOrder);
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public Optional<SalesOrder> getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    public SalesOrder updateStatus(Long id, SalesOrder.Status status) {
        SalesOrder salesOrder = getSalesOrderById(id)
                .orElseThrow(() -> new RuntimeException("Sales Order not found"));
        salesOrder.setStatus(status);
        return salesOrderRepository.save(salesOrder);
    }
}
