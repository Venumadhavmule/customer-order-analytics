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

	@Query(value = "SELECT MONTH(order_date) AS month, YEAR(order_date) AS year, COUNT(DISTINCT customer_id) AS total_customers "
			+ "FROM orders " + "GROUP BY YEAR(order_date), MONTH(order_date) "
			+ "ORDER BY YEAR(order_date), MONTH(order_date)", nativeQuery = true)
	List<Object[]> getTotalCustomersPerMonth();

	@Query(value = "SELECT COUNT(DISTINCT o1.customer_id) " + "FROM orders o1 " + "WHERE o1.customer_id IN ("
			+ "   SELECT DISTINCT o2.customer_id FROM orders o2 "
			+ "   WHERE MONTH(o2.order_date) = :previousMonth AND YEAR(o2.order_date) = :previousYear"
			+ ") AND MONTH(o1.order_date) = :currentMonth AND YEAR(o1.order_date) = :currentYear", nativeQuery = true)
	Long getReturningCustomers(@Param("currentMonth") int currentMonth, @Param("currentYear") int currentYear,
			@Param("previousMonth") int previousMonth, @Param("previousYear") int previousYear);

	@Query(value = "SELECT MONTH(o.order_date) AS month, " + "YEAR(o.order_date) AS year, "
			+ "COUNT(DISTINCT o.customer_id) AS total_customers, "
			+ "GROUP_CONCAT(DISTINCT c.name ORDER BY c.name SEPARATOR ', ') AS customer_names " + "FROM orders o "
			+ "JOIN customer c ON o.customer_id = c.id " + "GROUP BY YEAR(o.order_date), MONTH(o.order_date) "
			+ "ORDER BY YEAR(o.order_date), MONTH(o.order_date)", nativeQuery = true)
	List<Object[]> getTotalCustomersPerMonthWithNames();

}
