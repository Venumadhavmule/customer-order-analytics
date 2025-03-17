package com.venu.customerorderanalytics.dao;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.venu.customerorderanalytics.constant.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate orderDate;
	
	@Column(nullable = true, updatable = true)
	private LocalDate deliveryDate;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	private BigDecimal totalAmount;

	public Orders(LocalDate orderDate, LocalDate deliveryDate, OrderStatus status, Customer customer,
			BigDecimal totalAmount) {
		this.orderDate = orderDate;
		this.deliveryDate = deliveryDate;
		this.status = status;
		this.customer = customer;
		this.totalAmount = totalAmount;
	}
}
