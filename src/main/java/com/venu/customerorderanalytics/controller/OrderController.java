package com.venu.customerorderanalytics.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venu.customerorderanalytics.constant.OrderStatus;
import com.venu.customerorderanalytics.dao.Customer;
import com.venu.customerorderanalytics.dao.Orders;
import com.venu.customerorderanalytics.repository.CustomerRepository;
import com.venu.customerorderanalytics.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestBody OrderRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        Orders order = new Orders(null, customer, LocalDateTime.now(), OrderStatus.PENDING, request.getTotalAmount());
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        Orders order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.PENDING) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
