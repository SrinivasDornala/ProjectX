package com.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class OrderServiceExceptionhandler extends Exception{

	private static final long serialVersionUID = 1L;

	public OrderServiceExceptionhandler() {
		super();
	}
}
