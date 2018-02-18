package com.service;

import java.util.HashMap;
import java.util.Map;

import com.dto.Order;

public interface OrderServiceCache {
	public HashMap<String, Order> getFirstCache();
	public void setFirstCache(HashMap<String,  Order> firstCache);
	public Map<String, Order> addOrderstoCache();
	public Order checkforOrder(long id);
	public void putforOrder(Order order);
}
