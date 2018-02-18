package com.constants;

public interface OrderConstants {

	static final String INSERTORDER = "INSERT INTO orders " +
			"(orderId, customerName, isFulFilmentDone,noOfBricks,createddate) VALUES (?, ?, ?,?,?)";
	static final String GETORDER = "SELECT * FROM ORDERS WHERE orderId = ?";
	static final String ORDERS = "SELECT * from ORDERS";
	static final String UPDATESQL = "UPDATE Orders SET customerName = ?,noOfBricks=?,isFulFilmentDone=? WHERE orderId = ?";
	static final String UPDATEFULFILLMENT = "UPDATE Orders SET isFulFilmentDone=?, depachtedDate= CURDATE() WHERE orderId = ?";

	
}
