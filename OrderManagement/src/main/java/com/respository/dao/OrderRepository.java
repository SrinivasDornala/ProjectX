package com.respository.dao;

import java.util.List;
import java.util.Map;

import com.dto.Order;

public interface OrderRepository {
	public Order saveOrder(Order order);
	public Order getOrder(long orderid);
	public List<Order> getOrders();
	public Order updateOrder(Order order);
}
