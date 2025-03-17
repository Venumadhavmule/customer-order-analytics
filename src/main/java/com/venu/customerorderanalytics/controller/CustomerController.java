package com.venu.customerorderanalytics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venu.customerorderanalytics.dao.Customer;
import com.venu.customerorderanalytics.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerRepository.save(customer));
    }
}
