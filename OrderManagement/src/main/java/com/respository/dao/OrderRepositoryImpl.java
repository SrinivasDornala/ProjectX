package com.respository.dao;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.constants.OrderConstants;
import com.dto.Order;
import com.exception.OrderServiceException;

public class OrderRepositoryImpl implements OrderConstants,OrderRepository {

	@Autowired  
	JdbcTemplate jdbc; 
	
	@Override
	public Order saveOrder(Order order){
		System.out.println("OrderRepository.createOrder()"+ jdbc.queryForList("select * from Orders;"));
		try{
			if(order!=null)
			jdbc.update(INSERTORDER, new Object[] { order.getOrderId(),
					order.getCustomerName(),order.isFulfilmentDone(),order.getNoOfBricks(),new Date()
				});
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return order;
	}
	@Override
	public Order getOrder(long orderid){
		Order order= null;
		try{
			order=jdbc.queryForObject(GETORDER, new Object[] { orderid },
					new BeanPropertyRowMapper<>(Order.class));
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return order;
	}
	@Override
	@ExceptionHandler(OrderServiceException.class)
	public List<Order> getOrders(){
		List<Order> orders= null;
		try{
			orders= jdbc.query(ORDERS,new BeanPropertyRowMapper<Order>(Order.class));
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return orders;
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
				if(order.isFulfilmentDone()){
					resOrder.setFulfilmentDone(order.isFulfilmentDone());
					Object[] params = {order.isFulfilmentDone(),order.getOrderId()};
					int[] types = {Types.BOOLEAN,Types.VARCHAR};
					rows =jdbc.update(UPDATEFULFILLMENT,params,types);
				}
				else{
					Object[] params = { order.getCustomerName(), order.getNoOfBricks(),order.isFulfilmentDone(),order.getOrderId()};
					int[] types = {Types.VARCHAR, Types.INTEGER,Types.BOOLEAN,Types.VARCHAR};
					rows =jdbc.update(UPDATESQL,params,types);
				}
			}
		}catch(DataAccessException dte){
			throw new OrderServiceException();
		}
		return resOrder;
	}
	
	
}
