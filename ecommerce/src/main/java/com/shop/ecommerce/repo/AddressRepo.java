package com.shop.ecommerce.repo;

import com.shop.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface AddressRepo extends JpaRepository<Address, Integer> {
}
