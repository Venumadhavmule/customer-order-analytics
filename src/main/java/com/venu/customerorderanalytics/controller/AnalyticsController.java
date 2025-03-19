package com.venu.customerorderanalytics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.venu.customerorderanalytics.service.AnalyticsService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

	private final AnalyticsService analyticsService;

	public AnalyticsController(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@GetMapping("/profitable-customers")
	public ResponseEntity<List<Map<String, Object>>> getMostProfitableCustomers() {
		return ResponseEntity.ok(analyticsService.getMostProfitableCustomers());
	}

	@GetMapping("/monthly-retention")
	public ResponseEntity<List<Map<String, Object>>> getMonthlyRetentionRate() {
		return ResponseEntity.ok(analyticsService.getMonthlyRetentionRate());
	}

	@GetMapping("/longest-processing-orders")
	public ResponseEntity<List<Map<String, Object>>> getLongestOrderProcessingTimes() {
		return ResponseEntity.ok(analyticsService.getLongestOrderProcessingTimes());
	}

	@GetMapping("/product-category-avg")
	public ResponseEntity<Map<String, Double>> getBestWorstProductCategories() {
		return ResponseEntity.ok(analyticsService.getAverageOrderValueByCategory());
	}

	@GetMapping("/seasonal-patterns")
	public ResponseEntity<Map<String, Double>> getSeasonalOrderPatterns() {
		return ResponseEntity.ok(analyticsService.getSeasonalRevenue());
	}

	@GetMapping("/order-sizes")
	public ResponseEntity<Map<String, Integer>> getMostCommonOrderSizes() {
		return ResponseEntity.ok(analyticsService.getMostCommonOrderSizes());
	}
}
