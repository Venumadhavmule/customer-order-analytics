package com.venu.customerorderanalytics.dao;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category;
	private BigDecimal price;
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private Orders order;

	public OrderItem() {
	}

	public OrderItem(String category, BigDecimal price, int quantity, Orders order) {
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.order = order;
	}

	public String getCategory() {
		return category;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public Orders getOrder() {
		return order;
	}
}
