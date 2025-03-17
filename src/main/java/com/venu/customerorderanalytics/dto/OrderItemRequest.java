package com.venu.customerorderanalytics.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

	private String category;
	private BigDecimal price;
	private int quantity;

	public OrderItemRequest() {
	}

	public OrderItemRequest(String category, BigDecimal price, int quantity) {
		this.category = category;
		this.price = price;
		this.quantity = quantity;
	}
}
