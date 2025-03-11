package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Order;
import com.shop.ecommerce.entity.OrderedItems;
import com.shop.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderedItemsRepo extends JpaRepository<OrderedItems,Integer> {
     List<OrderedItems> findAllByOrder(Order order);
}
