package com.dto;


public class Order {

	private long orderId;
	private int noOfBricks;
	private String customerName;
	private boolean isFulfilmentDone;

	public boolean isFulfilmentDone() {
		return isFulfilmentDone;
	}
	public void setFulfilmentDone(boolean isFulfilmentDone) {
		this.isFulfilmentDone = isFulfilmentDone;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	public int getNoOfBricks() {
		return noOfBricks;
	}
	public void setNoOfBricks(int noOfBricks) {
		this.noOfBricks = noOfBricks;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
