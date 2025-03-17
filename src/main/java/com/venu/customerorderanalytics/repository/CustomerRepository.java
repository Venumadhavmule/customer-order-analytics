package com.venu.customerorderanalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venu.customerorderanalytics.dao.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}