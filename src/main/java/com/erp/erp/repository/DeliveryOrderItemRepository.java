package com.erp.erp.repository;

import com.erp.erp.model.DeliveryOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderItemRepository extends JpaRepository<DeliveryOrderItem, Long> {
    List<DeliveryOrderItem> findByDeliveryOrderId(Long deliveryOrderId);
}
