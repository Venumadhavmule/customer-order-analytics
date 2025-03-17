package com.venu.customerorderanalytics.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venu.customerorderanalytics.dao.Customer;
import com.venu.customerorderanalytics.dao.Orders;
import com.venu.customerorderanalytics.repository.CustomerRepository;
import com.venu.customerorderanalytics.service.OrderService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerRepository customerRepository;

	private final OrderService orderService;

	public CustomerController(CustomerRepository customerRepository, OrderService orderService) {
		this.customerRepository = customerRepository;
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
		return ResponseEntity.ok(customerRepository.save(customer));
	}

	@GetMapping("/{customerId}/orders")
	public ResponseEntity<List<Orders>> getCustomerOrders(@PathVariable Long customerId) {
		List<Orders> orders = orderService.getOrdersByCustomerId(customerId);
		return ResponseEntity.ok(orders);
	}
}
