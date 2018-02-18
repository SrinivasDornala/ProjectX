package com.controller;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dto.Order;
import com.exception.OrderServiceException;
import com.respository.dao.OrderRepositoryImpl;
import com.service.OrderServiceCacheImpl;

@RestController
@EnableAutoConfiguration
public class OrderController {

	@Autowired
	OrderRepositoryImpl orderRepository;
	@Autowired
	OrderServiceCacheImpl orderServiceCache;
	
	@RequestMapping("/welcome")
	public @ResponseBody String welcome(){
		System.out.println("OrderController.welcome()");
		return "WElcome";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value ="/neworder", method = RequestMethod.POST)
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		System.out.println("OrderController.createOrder()");
		long generatedId = generateID();
		Order resOrder =orderServiceCache.checkforOrder(generatedId);
		if(resOrder!= null)
			generatedId++;
		order.setOrderId(generatedId);
		try{
			orderRepository.saveOrder(order);
			if(order!= null)
				return new ResponseEntity<Order>(order,HttpStatus.OK);
		}catch(OrderServiceException ose){
			return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Order>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value ="/updateOrder/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Order> updateOrder(@PathParam(value = "id") long id,@RequestBody Order order) {
		Order newOrder= null;
		try{
			if(id>0){
				newOrder= orderRepository.getOrder(id);
				if(newOrder.isFulfilmentDone()) return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
				orderRepository.updateOrder(order);
			}
			if(order!= null)
				return new ResponseEntity<Order>(order,HttpStatus.OK);
		}catch(OrderServiceException ose){
			return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Order>(HttpStatus.BAD_REQUEST);
	}	
	@RequestMapping(value ="/fulfill/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Order> fulFillOrder(@PathParam(value = "id") long id) {
		
		Order order= null;
		try{
			if(id>0)
				order= orderRepository.getOrder(id);
			if(order!= null)
				order.setFulfilmentDone(true);
				order= orderRepository.updateOrder(order);
				return new ResponseEntity<Order>(order,HttpStatus.OK);
		}catch(OrderServiceException ose){
			return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping(value ="/getOrder/{id}")
	public ResponseEntity<Order> getOrder(@PathParam(value = "id") long id) {
		Order order= null;
		try{
			if(id>0){
				order= orderRepository.getOrder(id);
				return new ResponseEntity<Order>(order,HttpStatus.OK);
			}
			return new ResponseEntity<Order>(order,HttpStatus.NO_CONTENT);
		}catch(OrderServiceException ose){
			return new ResponseEntity<Order>(order,HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping(value ="/getOrders/")
	public ResponseEntity<Map<String, Order>> getOrders() {
		Map<String, Order> order= null;
		try{
				order= orderServiceCache.getFirstCache();
				if(order== null)
					order=orderServiceCache.addOrderstoCache();
				return new ResponseEntity<Map<String, Order>>(order,HttpStatus.OK);
		}catch(OrderServiceException ose){
			return new ResponseEntity<Map<String, Order>>(order,HttpStatus.BAD_REQUEST);
		}
	}
	
	public static  long  generateID() { 
	    Random rnd = new Random();
	    char [] digits = new char[11];
	    digits[0] = (char) (rnd.nextInt(9) + '1');
	    for(int i=1; i<digits.length; i++) {
	        digits[i] = (char) (rnd.nextInt(10) + '0');
	    }
	    return Long.parseLong(new String(digits));
	}
	
}
