package com.beaconfire.project22.controller;


import com.beaconfire.project22.Dao.OrderDao;
import com.beaconfire.project22.Dto.OrderDTO;
import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.OrderItem;
import com.beaconfire.project22.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order){
        order.setDatePlaced(LocalDateTime.now()// Set current date and time
);
        Order saveOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(saveOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) throws Exception {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) throws Exception {

        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order cancled successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error Cancel Order: " + e.getMessage());
        }
    }

    @GetMapping("/top-frequent/{userId}")
    public List<Object[]> getTop3FrequentItems(@PathVariable Long userId) {
        return orderService.getTop3FrequentlyPurchasedItems(userId);
    }

    @GetMapping("/top-recent/{userId}")
    public List<Object[]> getTop3RecentItems(@PathVariable Long userId) {
        return orderService.getTop3RecentlyPurchasedItems(userId);
    }


}
