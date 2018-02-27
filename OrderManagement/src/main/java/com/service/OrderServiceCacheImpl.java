package com.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dto.Order;
import com.exception.OrderServiceException;
import com.respository.dao.OrderRepository;
import com.respository.dao.OrderRepositoryImpl;

@Component
public class OrderServiceCacheImpl implements OrderServiceCache {
	@Autowired
	private OrderRepository orderRepository;
	
	 private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
     private final Lock rLock = readWriteLock.readLock();
     private final Lock wLock = readWriteLock.writeLock();
     
	private OrderServiceCacheImpl(){System.out.println("OrderServiceCacheImpl.OrderServiceCacheImpl()");}
	private OrderServiceCacheImpl orderServiceCache=null;
	
	public OrderServiceCacheImpl getInstance(){
		System.out.println("OrderServiceCacheImpl.getInstance()");
		if(orderServiceCache!=null)	return orderServiceCache;
		orderServiceCache= new OrderServiceCacheImpl();
		return orderServiceCache;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new CloneNotSupportedException();
	}
	
	private Map<String, Order> firstCache= new HashMap<String, Order>();
	private HashMap<String, Object> secondCache= new HashMap<String,Object>();
	
	@Override
	public Map<String, Order> getFirstCache() {
		try{
			rLock.lock();
			return firstCache;
		} finally {
	        rLock.unlock();
	    }
	}
	@Override
	public void setFirstCache(Map<String,  Order> firstCache) {
		try{
			wLock.lock();
			this.firstCache = firstCache;
		}  finally {
            wLock.unlock();
        }
	}
	@Override
	public Map<String, Order> addOrderstoCache(){
		Map<String, Order> list =orderRepository.getOrders();
		return this.firstCache =list;
	}
	@Override
	public Order checkforOrder(long id){
		try{
			rLock.lock();
			if(this.firstCache.containsKey(String.valueOf(id))){
				return (Order) this.firstCache.get(String.valueOf(id));
			}
		 } finally {
	         rLock.unlock();
	     }
		return null;
	}
	@Override
	public void putforOrder(Order order){
		try{
			wLock.lock();
			if(firstCache!= null){
				this.firstCache.put(String.valueOf( order.getOrderId()), order);
			}
		}  finally {
            wLock.unlock();
        }
	}
	@Override
	 public void clear() {
         wLock.lock();
         try {
             this.firstCache.clear();
         } finally {
             wLock.unlock();
         }
     }
	@Override
	 public Order remove(long id ) {
         wLock.lock();
         try {
             return this.firstCache.remove(String.valueOf(id));
         } finally {
             wLock.unlock();
         }
     }

	@Override
     public boolean containKey(long id ) {
         rLock.lock();
         try {
             return this.firstCache.containsKey(String.valueOf(id));
         } finally {
             rLock.unlock();
         }
     }
	
}
