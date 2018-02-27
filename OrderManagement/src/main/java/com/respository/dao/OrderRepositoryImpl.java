package com.respository.dao;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.constants.OrderConstants;
import com.dto.Order;
import com.exception.OrderServiceException;
import com.service.OrderServiceCache;

@Service
public class OrderRepositoryImpl implements OrderConstants,OrderRepository {

	@Autowired  
	JdbcTemplate jdbc; 
	
	@Autowired
	OrderServiceCache orderServiceCache;
	
	@Override
	public Order saveOrder(Order order){
		System.out.println("OrderRepository.createOrder()"+ jdbc.queryForList("select * from Orders;"));
		try{
			if(order!=null){
				order.setOrderId(System.currentTimeMillis());
				jdbc.update(INSERTORDER, new Object[] { order.getOrderId(),
					order.getCustomerName(),order.isFulfilmentDone(),order.getNoOfBricks(),new Date()
				});
				orderServiceCache.putforOrder(order);
			}
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return order;
	}
	@Override
	public Order getOrder(long orderid){
		Order order= null;
		try{
			if(orderServiceCache.getFirstCache().isEmpty()){
				order=jdbc.queryForObject(GETORDER, new Object[] { orderid },
					new BeanPropertyRowMapper<>(Order.class));
			}else{
				order= orderServiceCache.checkforOrder(orderid);
			}
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return order;
	}
	@Override
	@ExceptionHandler(OrderServiceException.class)
	public Map<String, Order> getOrders(){
		List<Order> orders= null;
		Map<String, Order> order= null;
		try{
			if(orderServiceCache.getFirstCache().isEmpty()){
				orders= jdbc.query(ORDERS,new BeanPropertyRowMapper<Order>(Order.class));
				for (int i = 0; i < orders.size(); i++) {
					order.put(String.valueOf(orders.get(i).getOrderId()), orders.get(i));
				}
				orderServiceCache.setFirstCache(order);
			}else{
				order= orderServiceCache.getFirstCache();
			}
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return order;
	}
	@Override
	public Order fulFillOrder(long id){
		Order resOrder= null;
		int rows= 0;
		try{
			resOrder= this.getOrder(id);
			if(resOrder!= null){
				resOrder.setFulfilmentDone(true);
				if(!resOrder.isFulfilmentDone() && !resOrder.isFulfilmentDone() ){
					resOrder.setFulfilmentDone(resOrder.isFulfilmentDone());
					Object[] params = {resOrder.isFulfilmentDone(),resOrder.getOrderId()};
					int[] types = {Types.BOOLEAN,Types.VARCHAR};
					rows =jdbc.update(UPDATEFULFILLMENT,params,types);
					orderServiceCache.putforOrder(resOrder);
				}
			}
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return resOrder;
	}
	
	@Override
	public Order updateOrder(Order order){
		Order resOrder= null;
		int rows= 0;
		try{
			resOrder= this.getOrder(order.getOrderId());
			if(resOrder!= null){
				resOrder.setCustomerName(order.getCustomerName());
				resOrder.setNoOfBricks(order.getNoOfBricks());
				if(!resOrder.isFulfilmentDone() && !order.isFulfilmentDone() ){
					Object[] params = { order.getCustomerName(), order.getNoOfBricks(),order.isFulfilmentDone(),order.getOrderId()};
					int[] types = {Types.VARCHAR, Types.INTEGER,Types.BOOLEAN,Types.VARCHAR};
					rows =jdbc.update(UPDATESQL,params,types);
					orderServiceCache.putforOrder(resOrder);
				}
			}
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return resOrder;
	}
}
