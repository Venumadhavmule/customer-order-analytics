package com.venu.customerorderanalytics.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venu.customerorderanalytics.dao.Orders;
import com.venu.customerorderanalytics.dto.OrderStatusUpdateRequest;
import com.venu.customerorderanalytics.repository.OrderRepository;
import com.venu.customerorderanalytics.service.OrderService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
    private final OrderRepository orderRepository;
    
    private final OrderService orderService;
   
    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateRequest request) {
        orderService.updateOrderStatus(id, request.getStatus());
        return ResponseEntity.ok("Order status updated successfully");
    }

}
