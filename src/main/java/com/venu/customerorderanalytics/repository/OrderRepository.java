package com.venu.customerorderanalytics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.venu.customerorderanalytics.dao.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByCustomerId(Long customerId);
}