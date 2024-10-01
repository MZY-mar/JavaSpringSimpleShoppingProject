package com.beaconfire.project22.customException;

public class ProductNotInWatchlistException extends RuntimeException {
    public ProductNotInWatchlistException(String message) {
        super(message);
    }
}
