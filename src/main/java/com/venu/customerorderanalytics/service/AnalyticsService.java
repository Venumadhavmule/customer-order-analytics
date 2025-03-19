package com.venu.customerorderanalytics.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.venu.customerorderanalytics.dao.Customer;
import com.venu.customerorderanalytics.repository.CustomerRepository;
import com.venu.customerorderanalytics.repository.OrderItemRepository;
import com.venu.customerorderanalytics.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final OrderItemRepository orderItemRepository;

	public List<Map<String, Object>> getMostProfitableCustomers() {
		List<Object[]> revenueData = orderRepository.getTotalRevenuePerCustomer();
		List<Object[]> refundData = orderRepository.getTotalRefundsPerCustomer();

		Map<Long, Double> totalSpent = new HashMap<>();
		Map<Long, Double> refunds = new HashMap<>();

		revenueData.forEach(row -> totalSpent.put((Long) row[0], ((BigDecimal) row[1]).doubleValue()));
		refundData.forEach(row -> refunds.put((Long) row[0], ((BigDecimal) row[1]).doubleValue()));

		List<Long> customerIds = new ArrayList<>(totalSpent.keySet());
		Map<Long, String> customerNames = customerRepository.findAllById(customerIds).stream()
				.collect(Collectors.toMap(Customer::getId, Customer::getName));

		return totalSpent.entrySet().stream().map(entry -> {
			long customerId = entry.getKey();
			double spent = entry.getValue();
			double refund = refunds.getOrDefault(customerId, 0.0);

			Map<String, Object> result = new HashMap<>();
			result.put("Customer", customerNames.getOrDefault(customerId, "Unknown"));
			result.put("Total Spent", spent);
			result.put("Refunded Amount", refund);
			result.put("Net Revenue", spent - refund);
			return result;
		}).sorted(Comparator.comparingDouble(c -> -(double) c.get("Net Revenue"))).limit(3)
				.collect(Collectors.toList());
	}

	public List<Map<String, Object>> getMonthlyRetentionRate() {
		List<Object[]> results = orderRepository.getTotalCustomersPerMonth();

		List<Map<String, Object>> retentionData = new ArrayList<>();

		for (Object[] row : results) {
			Integer month = ((Number) row[0]).intValue();
			Integer year = ((Number) row[1]).intValue();
			Long totalCustomers = ((Number) row[2]).longValue();

			Map<String, Object> monthData = new HashMap<>();
			monthData.put("month", month);
			monthData.put("year", year);
			monthData.put("totalCustomers", totalCustomers);

			retentionData.add(monthData);
		}

		return retentionData;
	}

	public List<Map<String, Object>> getLongestOrderProcessingTimes() {
		return orderRepository.findDeliveredOrders().stream()
				.filter(order -> order.getOrderDate() != null && order.getDeliveryDate() != null)

				.sorted(Comparator.comparingLong(
						order -> -ChronoUnit.DAYS.between(order.getOrderDate(), order.getDeliveryDate())))
				.limit(5).map(order -> {
					Map<String, Object> result = new HashMap<>();
					result.put("Order ID", order.getId());
					result.put("Order Date", order.getOrderDate());
					result.put("Delivery Date", order.getDeliveryDate());
					result.put("Processing Time (Days)",
							ChronoUnit.DAYS.between(order.getOrderDate(), order.getDeliveryDate()));

					log.info("Longest Longest Order Processing: {}", result.toString());
					return result;
				}).collect(Collectors.toList());
	}

	public Map<String, Double> getAverageOrderValueByCategory() {
		List<Object[]> results = orderItemRepository.getOrderValuePerCategory();

		return results.stream()
				.collect(Collectors.toMap(row -> (String) row[0], row -> ((Number) row[1]).doubleValue()));
	}

	public Map<String, Double> getSeasonalRevenue() {
		List<Object[]> revenueByMonth = orderRepository.getRevenuePerMonth();
		Map<String, Double> seasonalRevenue = new HashMap<>();

		revenueByMonth.forEach(row -> {
			String season = getSeason((int) row[0]);
			seasonalRevenue.put(season,
					seasonalRevenue.getOrDefault(season, 0.0) + ((BigDecimal) row[1]).doubleValue());
		});

		return seasonalRevenue;
	}

	private String getSeason(int month) {
		if (month >= 3 && month <= 5)
			return "Spring";
		if (month >= 6 && month <= 8)
			return "Summer";
		if (month >= 9 && month <= 11)
			return "Fall";
		return "Winter";
	}

	public Map<String, Integer> getMostCommonOrderSizes() {
		List<Object[]> orderData = orderRepository.getOrderItemCounts();

		Map<String, Integer> orderSizeCounts = new HashMap<>();
		orderSizeCounts.put("Small", 0);
		orderSizeCounts.put("Medium", 0);
		orderSizeCounts.put("Large", 0);

		for (Object[] row : orderData) {
			int totalItems = ((Number) row[1]).intValue();

			if (totalItems >= 1 && totalItems <= 2) {
				orderSizeCounts.put("Small", orderSizeCounts.get("Small") + 1);
			} else if (totalItems >= 3 && totalItems <= 5) {
				orderSizeCounts.put("Medium", orderSizeCounts.get("Medium") + 1);
			} else {
				orderSizeCounts.put("Large", orderSizeCounts.get("Large") + 1);
			}
		}

		return orderSizeCounts;
	}
}
