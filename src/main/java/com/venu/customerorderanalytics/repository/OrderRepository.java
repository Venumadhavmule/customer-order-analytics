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

	@Query("SELECT o FROM Orders o WHERE o.status = 'DELIVERED'")
	List<Orders> findDeliveredOrders();

	@Query("SELECT MONTH(o.orderDate), SUM(o.totalAmount) FROM Orders o GROUP BY MONTH(o.orderDate)")
	List<Object[]> getRevenuePerMonth();

	@Query("SELECT o FROM Orders o WHERE o.customer.id = :customerId")
	List<Orders> findOrdersByCustomerId(@Param("customerId") Long customerId);

	@Query(value = "SELECT MONTH(order_date) AS month, YEAR(order_date) AS year, COUNT(DISTINCT customer_id) AS total_customers "
			+ "FROM orders " + "GROUP BY YEAR(order_date), MONTH(order_date) "
			+ "ORDER BY YEAR(order_date), MONTH(order_date)", nativeQuery = true)
	List<Object[]> getTotalCustomersPerMonth();

	@Query(value = "SELECT o.id AS order_id, SUM(oi.quantity) AS total_items " + "FROM orders o "
			+ "JOIN order_items oi ON o.id = oi.order_id " + "GROUP BY o.id", nativeQuery = true)
	List<Object[]> getOrderItemCounts();

}
