package com.beaconfire.project22.controller;


import com.beaconfire.project22.Dao.OrderDao;
import com.beaconfire.project22.Dto.OrderDTO;
import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Optional<Order> order = orderDao.findById(id);
//        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order canceled successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
