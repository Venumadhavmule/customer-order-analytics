package com.venu.customerorderanalytics.dto;


import com.venu.customerorderanalytics.constant.OrderStatus;

import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus status;

}
