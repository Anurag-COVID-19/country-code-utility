package com.planet.payments.countrycodeutility.service_impl;

import com.planet.payments.countrycodeutility.entity.Order;
import com.planet.payments.countrycodeutility.repository.OrdersRepository;
import com.planet.payments.countrycodeutility.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersRepository repository;
    /**
     * This is to fetch list of orders
     * @return list of orders
     */
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        repository.findAll().forEach(orders::add);
        return orders;
    }
}
