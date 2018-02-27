package com.respository.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dto.Order;

@Component
public interface OrderRepository {
	public Order saveOrder(Order order);
	public Order getOrder(long orderid);
	public Map<String, Order> getOrders();
	public Order updateOrder(Order order);
	public Order fulFillOrder(long id);
}
