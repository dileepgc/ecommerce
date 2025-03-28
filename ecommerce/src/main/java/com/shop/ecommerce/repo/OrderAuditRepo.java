package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.OrderedAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderAuditRepo extends JpaRepository<OrderedAudit,Integer> {
    OrderedAudit findByOrder(Order order);

    List<OrderedAudit> findAllByOrder(Order order);
}
