package com.beaconfire.project22.controller;


import java.util.List;

import com.beaconfire.project22.Dto.WatchListDTO;
import com.beaconfire.project22.Service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    @Autowired
    private WatchListService watchlistService;

    // Add a product to the watchlist
    @PostMapping("/add")
    public ResponseEntity<String> addProductToWatchlist(@PathVariable WatchListDTO request) {
        watchlistService.addProductToWatchlist(request.getUserId(), request.getProductId());
        return ResponseEntity.ok("Product added to watchlist.");
    }

    // Remove a product from the watchlist
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromWatchlist(@RequestBody WatchListDTO request) {
        watchlistService.removeProductFromWatchlist(request.getUserId(), request.getProductId());
        return ResponseEntity.ok("Product removed from watchlist.");
    }

    // Get in-stock products in the user's watchlist
    @GetMapping("/in-stock/{userId}")
    public ResponseEntity<List<Long>> getInStockProductsInWatchlist(@RequestParam Long userId) {
        List<Long> products = watchlistService.getInStockProductsInWatchlist(userId);
        return ResponseEntity.ok(products);
    }
}
