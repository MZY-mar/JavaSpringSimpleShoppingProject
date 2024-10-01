package com.beaconfire.project22.controller;
import com.beaconfire.project22.Dto.OrderDTO;
import com.beaconfire.project22.Model.Product;
import com.beaconfire.project22.Service.OrderService;
import com.beaconfire.project22.Service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @GetMapping("/orders/userid/{userId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUser(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/details/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<Optional<Product>> getProductDetails(@PathVariable Long id) {
        Optional<Product> product = productService.getProductDetails(id);
        return ResponseEntity.ok(product);
    }
    @PostMapping("/products/add")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok("Product added successfully. ID: " + newProduct.getProductId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
        }
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {
            productService.updateProductDetails(id, updatedProduct);
            return ResponseEntity.ok("Product updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting product: " + e.getMessage());
        }
    }
    @DeleteMapping("/orders/delete/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);  // You need to implement this in OrderService
            return ResponseEntity.ok("Order deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting order: " + e.getMessage());
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProductList();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/orders/{orderId}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId, @RequestParam String status) {
        try {
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Order status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }







}
