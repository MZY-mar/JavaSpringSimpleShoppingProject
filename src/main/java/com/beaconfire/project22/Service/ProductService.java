package com.beaconfire.project22.Service;

import com.beaconfire.project22.Dao.ProductDao;
import com.beaconfire.project22.Model.Product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    //Get all available products (in stock only quantity > 0)
    public List<Product> getAvailableProducts() {
        return productDao.findByQuantityGreaterThan(0);
    }
    public List<Product> getAllProductList() {
        return productDao.getAll();
    }

    // Get product details
    public Optional<Product> getProductDetails(Long productId) {
        return Optional.ofNullable(productDao.findById(productId));
    }

    @Transactional
    public void deleteProduct(Long productId) throws Exception {
         Product product = productDao.findById(productId);
        if (product == null) {
            throw new Exception("Product not found");
        }
        productDao.delete(product);
    }
    @Transactional
    public Product addProduct(Product product) {
        try {
            productDao.add(product);
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Transactional
    public Product updateProductDetails(Long productId, Product updatedProduct) throws Exception {
        Product product = productDao.findById(productId);
        if(product == null){
            throw new Exception("No product exist");
        }
        // Update product details
        product.setWholesalePrice(updatedProduct.getWholesalePrice());
        product.setName(updatedProduct.getName());
        product.setRetailPrice(updatedProduct.getRetailPrice());
        product.setDescription(updatedProduct.getDescription());
        product.setQuantity(updatedProduct.getQuantity());

        return productDao.saveOrUpdate(product);  // Save the updated product
    }

    public boolean isProductInStock(Long productId) {
        return  productDao.existsByProductIdAndQuantityGreaterThan(productId,0);
    }


}
