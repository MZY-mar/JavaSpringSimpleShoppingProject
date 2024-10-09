package com.beaconfire.project22.Dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long orderId;
    private String orderStatus;
    private Long userId;
    private   LocalDateTime datePlaced;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Long orderId, String orderStatus, Long userId,
                    List<OrderItemDTO> orderItems, LocalDateTime placed) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.userId = userId;
        this.orderItems = orderItems;
        this.datePlaced = placed;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(LocalDateTime datePlaced) {
        this.datePlaced = datePlaced;
    }
}
