package com.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.dto.Order;

@Component
public interface OrderServiceCache {
	public Map<String, Order> getFirstCache();
	public void setFirstCache(Map<String, Order> order);
	public Map<String, Order> addOrderstoCache();
	public Order checkforOrder(long id);
	public void putforOrder(Order order);
	public OrderServiceCache getInstance();
	public boolean containKey(long id );
	public Order remove(long id );
	public void clear();
}
