package com.repository;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.dto.Order;
import com.exception.OrderServiceException;
import com.respository.dao.OrderRepository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderRepositoryTest {

    @Mock
    OrderRepository orderRepository;

    public OrderRepositoryTest() {
    }


    @Test
    public void saveOrder() {
        Order order = this.mockOrder("MyName");
        when(orderRepository.saveOrder(order))
                .thenReturn(order);

        Order order1= orderRepository.saveOrder(order);
        assertTrue(order1.getOrderId() > 1);
        assertTrue(order1.getCustomerName().equals("MyName"));
    }

    @Test
    public void getOrder() {
    	 Order order = this.mockOrder("MyName");

        when(order.getOrderId())
                .thenReturn(1234567890l);

        Order result = orderRepository.getOrder(order.getOrderId());
        assertNotNull("saved post id should not be null", result.getOrderId());
        assertTrue(result.getCustomerName().equals("MyName"));
    }

    @Test
    public void updateOrder() {
    	Order order = this.mockOrder("NewName");
    	when(order.getOrderId())
        .thenReturn(1234567890l);

    	Order result = orderRepository.updateOrder(order);
        assertNotNull("saved Order id should not be null", result.getOrderId());
        assertTrue(result.getCustomerName().equals("MyName"));
    }

    @Test
    public void fulFillOrder() {
    	Order order = this.mockOrder("MyName");
    	when(order.getOrderId())
        .thenReturn(1234567890l);

    	Order result = orderRepository.fulFillOrder(order.getOrderId());
        assertNotNull("saved Order id should not be null", result.getOrderId());
        assertTrue(result.getCustomerName().equals("MyName"));
    }
    @Test(expected = OrderServiceException.class)
    public void getOrders() {
    	Map<String, Order> result = orderRepository.getOrders();
    	assertTrue("Order's size is 1", result.size() > 1);
    	assertTrue( result.get("1234567890").getCustomerName().equals("MyName") );
    }

    @Test
    public void testgetOrder() {
    	Order order = this.mockOrder("MyName");
    	when(order.getOrderId())
        .thenReturn(null);
    	Order result =orderRepository.getOrder(order.getOrderId());
    	
    	assertNull(result.getOrderId());
    }


    private Order mockOrder(String prefix) {
    	Order r = new Order();
        r.setNoOfBricks(100);
        r.setCustomerName(prefix);
        return r;
    }
}