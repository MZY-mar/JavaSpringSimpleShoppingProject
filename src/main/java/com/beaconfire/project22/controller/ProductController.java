package com.beaconfire.project22.controller;

import com.beaconfire.project22.Model.Product;
import com.beaconfire.project22.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all product
    @GetMapping
    public List<Product> getAllAvailableProducts() {
        return productService.getAvailableProducts();
    }

    // Get one product detail by click the link for specific product and then
    // redirect to the detail page
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductDetails(@PathVariable Long id) {
        return productService.getProductDetails(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
