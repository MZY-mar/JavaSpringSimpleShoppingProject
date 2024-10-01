package com.beaconfire.project22.Service;


import com.beaconfire.project22.Dao.WatchListDao;
import com.beaconfire.project22.Model.WatchList;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchListService {

    @Autowired
    private WatchListDao watchlistDao;

    @Autowired
    private ProductService productService; // Assume this service exists for managing products

    // Add a product to the watchlist
    public WatchList addProductToWatchlist(Long userId, Long productId) {
        Optional<WatchList> existingWatchlist = watchlistDao.findByUserIdAndProductId(userId, productId);
        if (existingWatchlist.isPresent()) {
            throw new RuntimeException("Product is already in the watchlist");
        }
        WatchList watchlist = new WatchList(userId, productId);
        return watchlistDao.save(watchlist);
    }

    // Remove a product from the watchlist
    public void removeProductFromWatchlist(Long userId, Long productId) {
        Optional<WatchList> existingWatchlist =
                watchlistDao.findByUserIdAndProductId(userId, productId);
        if (existingWatchlist.isEmpty()) {
            throw new RuntimeException("Product is not in the watchlist");
        }

        watchlistDao.delete(existingWatchlist.get());
    }

    // in-stock products in the watchlist
    public List<Long> getInStockProductsInWatchlist(Long userId) {
        List<WatchList> watchlist = watchlistDao.findByUserId(userId);
        return watchlist.stream()
                .filter(item -> productService.isProductInStock(item.getProductId()))  // Assuming this method exists
                .map(WatchList::getProductId)
                .collect(Collectors.toList());
    }
}