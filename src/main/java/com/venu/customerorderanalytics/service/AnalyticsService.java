package com.venu.customerorderanalytics.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
	    List<Object[]> results = orderRepository.getTotalCustomersPerMonthWithNames();
	    
	    List<Map<String, Object>> retentionData = new ArrayList<>();
	    
	    for (Object[] row : results) {
	        Integer month = ((Number) row[0]).intValue();  
	        Integer year = ((Number) row[1]).intValue();   
	        Long totalCustomers = ((Number) row[2]).longValue();  
	        String customerNames = (String) row[3];  

	        Map<String, Object> monthData = new HashMap<>();
	        monthData.put("month", month);
	        monthData.put("year", year);
	        monthData.put("totalCustomers", totalCustomers);
	        monthData.put("customerNames", customerNames != null ? customerNames : "No customers");

	        retentionData.add(monthData);
	    }
	    
	    return retentionData;
	}


	public List<Map<String, Object>> getLongestOrderProcessingTimes() {
	    return orderRepository.findDeliveredOrders().stream()
	        .filter(order -> order.getOrderDate() != null && order.getDeliveryDate() != null)  // Ensure no null dates
	        .sorted(Comparator.comparingLong(order -> 
	            -ChronoUnit.DAYS.between(order.getOrderDate(), order.getDeliveryDate())  // Sort in descending order
	        ))
	        .limit(5)
	        .map(order -> {
	            Map<String, Object> result = new HashMap<>();
	            result.put("Order ID", order.getId());
	            result.put("Order Date", order.getOrderDate());
	            result.put("Delivery Date", order.getDeliveryDate());
	            result.put("Processing Time (Days)", 
	                ChronoUnit.DAYS.between(order.getOrderDate(), order.getDeliveryDate())
	            );
	            
	            log.info("Longest Longest Order Processing: {}",result.toString());
	            return result;
	        })
	        .collect(Collectors.toList());
	}


	public Map<String, Double> getAverageOrderValueByCategory() {
	    List<Object[]> results = orderItemRepository.getAverageOrderValueByCategory();  // Fetch query results

	    return results.stream().collect(Collectors.toMap(
	        row -> (String) row[0],
	        row -> ((Number) row[1]).doubleValue()
	    ));
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

	public Map<String, Integer> getOrderSizeDistribution() {
		List<Object[]> orderSizes = orderItemRepository.getOrderCategoryCounts();
		Map<String, Integer> distribution = new HashMap<>();

		orderSizes.forEach(row -> {
			int itemCount = (int) row[0];
			int count = ((Long) row[1]).intValue();
			String category = itemCount <= 2 ? "Small" : itemCount <= 5 ? "Medium" : "Large";
			distribution.put(category, distribution.getOrDefault(category, 0) + count);
		});

		return distribution;
	}
}
