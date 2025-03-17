package com.venu.customerorderanalytics.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.venu.customerorderanalytics.constant.OrderStatus;
import com.venu.customerorderanalytics.dao.Customer;
import com.venu.customerorderanalytics.dao.OrderItem;
import com.venu.customerorderanalytics.dao.Orders;
import com.venu.customerorderanalytics.dto.OrderItemRequest;
import com.venu.customerorderanalytics.dto.OrderRequest;
import com.venu.customerorderanalytics.repository.CustomerRepository;
import com.venu.customerorderanalytics.repository.OrderItemRepository;
import com.venu.customerorderanalytics.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final OrderItemRepository orderItemRepository;

	public Orders placeOrder(OrderRequest orderRequest) {
		Customer customer = customerRepository.findById(orderRequest.getCustomerId())
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		Orders order = new Orders(LocalDate.now(), null, OrderStatus.PENDING, customer, calculateTotal(orderRequest));

		orderRepository.save(order);

		for (OrderItemRequest itemRequest : orderRequest.getItems()) {
			OrderItem item = new OrderItem(itemRequest.getCategory(), itemRequest.getPrice(), itemRequest.getQuantity(),
					order);
			orderItemRepository.save(item);
		}

		return order;
	}

	public List<Orders> getAllOrders() {
		return orderRepository.findAll();
	}

	public Orders getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	public void cancelOrder(Long id) {
		Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

		if (order.getStatus() == OrderStatus.PENDING) {
			order.setStatus(OrderStatus.CANCELED);
			orderRepository.save(order);
		} else {
			throw new RuntimeException("Only PENDING orders can be canceled");
		}
	}

	private BigDecimal calculateTotal(OrderRequest orderRequest) {
		return orderRequest.getItems().stream()
				.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public void updateOrderStatus(Long id, OrderStatus newStatus) {
		Orders order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

		OrderStatus currentStatus = order.getStatus();

		if (currentStatus == OrderStatus.CANCELED) {
			throw new RuntimeException("Cannot update status of a canceled order");
		}

		if (currentStatus == OrderStatus.DELIVERED) {
			throw new RuntimeException("Cannot update status of a delivered order");
		}

		if (currentStatus == OrderStatus.PENDING && newStatus == OrderStatus.SHIPPED
				|| currentStatus == OrderStatus.SHIPPED && newStatus == OrderStatus.DELIVERED) {

			order.setStatus(newStatus);
			orderRepository.save(order);
		} else {
			throw new RuntimeException("Invalid status transition");
		}
	}

	public List<Orders> getOrdersByCustomerId(Long customerId) {
		return orderRepository.findByCustomerId(customerId);
	}

}
