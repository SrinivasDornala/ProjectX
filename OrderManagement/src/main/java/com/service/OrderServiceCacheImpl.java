package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dto.Order;
import com.exception.OrderServiceException;
import com.respository.dao.OrderRepositoryImpl;

@Component
public class OrderServiceCacheImpl implements OrderServiceCache {
	@Autowired
	private OrderRepositoryImpl orderRepository;
	
	private OrderServiceCacheImpl(){}
	private OrderServiceCacheImpl orderServiceCache=null;
	public OrderServiceCacheImpl getInstance(){
		if(orderServiceCache!=null)	return orderServiceCache;
		orderServiceCache= new OrderServiceCacheImpl();
		return orderServiceCache;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new CloneNotSupportedException();
	}
	
	private HashMap<String, Order> firstCache= new HashMap<String, Order>();
	private HashMap<String, Object> secondCache= new HashMap<String,Object>();
	
	@Override
	public HashMap<String, Order> getFirstCache() {
		return firstCache;
	}
	@Override
	public void setFirstCache(HashMap<String,  Order> firstCache) {
		this.firstCache = firstCache;
	}
	@Override
	public Map<String, Order> addOrderstoCache(){
		List<Order> list =orderRepository.getOrders();
		for (int i = 0; i < list.size(); i++) {
			firstCache.put(String.valueOf(list.get(i).getOrderId()), list.get(i));
		}
		return this.firstCache;
	}
	@Override
	public Order checkforOrder(long id){
		if(this.firstCache.containsKey(String.valueOf(id))){
			return (Order) this.firstCache.get(String.valueOf(id));
		}
		return null;
	}
	@Override
	public void putforOrder(Order order){
		if(firstCache!= null){
			this.firstCache.put(String.valueOf( order.getOrderId()), order);
		}
	}
	
}
