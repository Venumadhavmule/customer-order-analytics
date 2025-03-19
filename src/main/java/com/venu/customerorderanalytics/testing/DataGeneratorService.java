package com.venu.customerorderanalytics.testing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.venu.customerorderanalytics.constant.OrderStatus;
import com.venu.customerorderanalytics.dao.Customer;
import com.venu.customerorderanalytics.dao.OrderItem;
import com.venu.customerorderanalytics.dao.Orders;
import com.venu.customerorderanalytics.repository.CustomerRepository;
import com.venu.customerorderanalytics.repository.OrderItemRepository;
import com.venu.customerorderanalytics.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Service
public class DataGeneratorService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	private final Faker faker = new Faker();
	private final Random random = new Random();

	private static final List<String> CATEGORIES = Arrays.asList("Electronics", "Clothing", "Books", "Home & Kitchen",
			"Toys");

	@Transactional
	public void generateTestData() {
		generateCustomers(100);
		generateOrders(500);
	}

	private void generateCustomers(int count) {
		List<Customer> customers = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			customers.add(new Customer(faker.name().fullName(), faker.internet().emailAddress(), LocalDateTime.now()));
		}
		customerRepository.saveAll(customers);
	}

	private void generateOrders(int count) {
		List<Customer> customers = customerRepository.findAll();
		List<Orders> orders = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			Customer customer = customers.get(random.nextInt(customers.size()));
			LocalDate orderDate = generateRandomDate();

			OrderStatus status = getRandomStatus();

			LocalDate deliveryDate = (status == OrderStatus.DELIVERED) ? orderDate.plusDays(random.nextInt(10) + 1)
					: null;

			BigDecimal totalAmount = new BigDecimal(faker.commerce().price(10.0, 500.0));

			Orders order = new Orders(orderDate, deliveryDate, status, customer, totalAmount);
			orders.add(order);
		}

		orderRepository.saveAll(orders);
		generateOrderItems(orders);
	}

	private void generateOrderItems(List<Orders> orders) {
		List<OrderItem> orderItems = new ArrayList<>();

		for (Orders order : orders) {
			int itemCount = random.nextInt(9) + 2;
			for (int j = 0; j < itemCount; j++) {
				
				OrderItem item = new OrderItem(CATEGORIES.get(random.nextInt(CATEGORIES.size())),
						new BigDecimal(faker.commerce().price(5.0, 200.0)), random.nextInt(5) + 1, order);
				orderItems.add(item);
			}
		}

		orderItemRepository.saveAll(orderItems);
	}

	private LocalDate generateRandomDate() {
		return LocalDate.now().minusDays(random.nextInt(365));
	}

	private OrderStatus getRandomStatus() {
		
		List<OrderStatus> statuses = Arrays.asList(OrderStatus.PENDING, OrderStatus.SHIPPED, OrderStatus.DELIVERED,
				OrderStatus.CANCELED);
		
		return statuses.get(random.nextInt(statuses.size()));
	} 
}
