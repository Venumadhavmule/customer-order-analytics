package com.venu.customerorderanalytics.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.venu.customerorderanalytics.constant.OrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    private LocalDateTime orderDate = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private BigDecimal totalAmount;
}
