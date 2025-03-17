package com.venu.customerorderanalytics.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderRequest {

	private Long customerId;

	private List<OrderItemRequest> items;

	public OrderRequest() {
	}

	public OrderRequest(Long customerId, List<OrderItemRequest> items) {
		this.customerId = customerId;
		this.items = items;
	}
}
