package com.planet.payments.countrycodeutility.repository;

import com.planet.payments.countrycodeutility.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, String> {
}
