package com.venu.customerorderanalytics.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venu.customerorderanalytics.dao.Orders;
import com.venu.customerorderanalytics.dto.OrderRequest;
import com.venu.customerorderanalytics.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Orders> placeOrder(@RequestBody OrderRequest orderRequest) {
		return ResponseEntity.ok(orderService.placeOrder(orderRequest));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
		orderService.cancelOrder(id);
		return ResponseEntity.ok("Order canceled successfully");
	}
}
