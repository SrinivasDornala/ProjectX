/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.dto.Order;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.OrderManagementApplication;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderManagementApplication.class)
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private Order order;
    
    @Before
    public void Setup(){
    	order.setCustomerName("MyName");
    	order.setOrderId(System.currentTimeMillis());
    	order.setNoOfBricks(100);
    }
    
    private Order mockOrder(String prefix) {
    	Order r = new Order();
        r.setNoOfBricks(100);
        r.setCustomerName(prefix);
        r.setOrderId(new Random().nextInt(10));
        return r;
    }

  @Test
    public void newOrderTest() throws Exception {

	  Order or = mockOrder("MyName");
	  ObjectMapper map = new ObjectMapper();
      String r1Json = map.writeValueAsString(or);
      System.out.println(r1Json);
       
      	 this.mockMvc.perform(get("/newOrder")
        			.content(r1Json)
        			.contentType(MediaType.APPLICATION_JSON)
        			.accept(MediaType.APPLICATION_JSON))
        			.andExpect(status().isOk());
      	
      	 ResponseEntity<Order> entity = new TestRestTemplate()
                 .getForEntity("http://localhost:8080/Ordercontroller/newOrder", Order.class);
         assertEquals(HttpStatus.OK, entity.getStatusCode());
         assertEquals("running", entity.getBody().getOrderId());
    }

  @Test	
    public void updateOrderTest() throws Exception {
	  	Order order = this.mockOrder("NewName");
	  	order.setOrderId(1234567890l);
        ResultActions resultActions=this.mockMvc.perform(get("/updateOrder/1234567890").contentType(MediaType.APPLICATION_JSON).param("customerName", order.getCustomerName()))
                .andDo(print()).andExpect(status().isOk());
       resultActions
    			.andExpect( status().isOk() )
    			.andExpect( content().contentType( "text/plain;charset=UTF-8" ) )
       			.andExpect(jsonPath("$.orderId", is(1234567890)))
       			.andExpect(jsonPath("$.customerName", is("NewName")))
       			.andExpect(jsonPath("$.noOfBricks", is(100)));
    }
    
  @Test	
  public void fulFillOrderTest() throws Exception {
	  	Order order = this.mockOrder("MyName");
	  	order.setOrderId(1234567890l);
	  	order.setFulfilmentDone(true);
	  	 ResultActions resultActions=this.mockMvc.perform(get("/fulfillorder/1234567890").contentType(MediaType.APPLICATION_JSON))
              .andDo(print()).andExpect(status().isOk());
	  	resultActions
		.andExpect( status().isOk() )
		.andExpect( content().contentType( "text/plain;charset=UTF-8" ) )
		.andExpect(jsonPath("$.orderId", is(1234567890)))
		.andExpect(jsonPath("$.customerName", is("MyName")))
		.andExpect(jsonPath("$.noOfBricks", is(100)))
		.andExpect(jsonPath("$.isFulfilmentDone", is(true)));
	  	
  }

  @Test	
  public void getOrderTest() throws Exception {
	  	 ResultActions resultActions=this.mockMvc.perform(get("/getOrder/1234567890").contentType(MediaType.APPLICATION_JSON))
              .andDo(print()).andExpect(status().isOk());
	  	resultActions
		.andExpect( status().isOk() )
		.andExpect( content().contentType( "text/plain;charset=UTF-8" ) )
	  	.andExpect(jsonPath("$.orderId", is(1234567890)))
		.andExpect(jsonPath("$.customerName", is("MyName")))
		.andExpect(jsonPath("$.noOfBricks", is(100)))
		.andExpect(jsonPath("$.isFulfilmentDone", is(true)));
	  	
  }

  @Test	
  public void getOrdersTest() throws Exception {
	  	 ResultActions resultActions=this.mockMvc.perform(get("/getOrders").contentType(MediaType.APPLICATION_JSON))
              .andDo(print()).andExpect(status().isOk());
	  	String result =resultActions
		.andExpect( status().isOk() )
		.andExpect( content().contentType( "text/plain;charset=UTF-8" ) )
		.andReturn().getResponse().getContentAsString();
	  	
  }
 
}
