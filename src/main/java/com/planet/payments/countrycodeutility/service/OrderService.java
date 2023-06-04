package com.planet.payments.countrycodeutility.service;

import com.planet.payments.countrycodeutility.entity.Order;

import java.util.List;

public interface OrderService
{
    /**
     * This is to fetch list of orders
     * @return list of orders
     */
    public List<Order> getAllOrders();
}
