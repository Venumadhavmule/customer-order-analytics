package com.venu.customerorderanalytics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.venu.customerorderanalytics.dao.Orders;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

	@Query("SELECT o.customer.id, SUM(o.totalAmount) FROM Orders o GROUP BY o.customer.id")
	List<Object[]> getTotalRevenuePerCustomer();

	@Query("SELECT o.customer.id, SUM(o.totalAmount) FROM Orders o WHERE o.status = 'REFUNDED' GROUP BY o.customer.id")
	List<Object[]> getTotalRefundsPerCustomer();

	@Query("SELECT MONTH(o.orderDate), o.customer.id FROM Orders o GROUP BY MONTH(o.orderDate), o.customer.id")
	List<Object[]> getDistinctCustomersPerMonth();

	@Query("SELECT o FROM Orders o WHERE o.status = 'DELIVERED'")
	List<Orders> findDeliveredOrders();

	@Query("SELECT MONTH(o.orderDate), SUM(o.totalAmount) FROM Orders o GROUP BY MONTH(o.orderDate)")
	List<Object[]> getRevenuePerMonth();
	
	@Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId")
	List<Orders> findByCustomerId(@Param("customerId") Long customerId);
}
