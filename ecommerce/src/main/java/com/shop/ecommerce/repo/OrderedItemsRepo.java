package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.OrderedItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface OrderedItemsRepo extends JpaRepository<OrderedItems,Integer> {
}
