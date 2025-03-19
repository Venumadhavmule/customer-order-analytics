package com.venu.customerorderanalytics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.venu.customerorderanalytics.dao.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	@Query("SELECT oi.category, SUM(oi.price * oi.quantity) FROM OrderItem oi GROUP BY oi.category")
	List<Object[]> getOrderValuePerCategory();

	@Query("SELECT oi.category, COUNT(oi) FROM OrderItem oi GROUP BY oi.category")
	List<Object[]> getOrderCategoryCounts();

	@Query("SELECT oi.category, SUM(oi.price * oi.quantity) FROM OrderItem oi GROUP BY oi.category")
	List<Object[]> getAverageOrderValueByCategory();

}