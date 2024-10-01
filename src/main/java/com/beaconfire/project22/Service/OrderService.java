package com.beaconfire.project22.Service;

import com.beaconfire.project22.Dao.OrderDao;
import com.beaconfire.project22.Dao.OrderItemDao;
import com.beaconfire.project22.Dao.ProductDao;
import com.beaconfire.project22.Dao.UserDao;
import com.beaconfire.project22.Dto.OrderDTO;
import com.beaconfire.project22.Dto.OrderItemDTO;
import com.beaconfire.project22.Model.Order;
import com.beaconfire.project22.Model.OrderItem;
import com.beaconfire.project22.Model.Product;
import com.beaconfire.project22.Model.Users;
import com.beaconfire.project22.customException.NotEnoughInventoryException;
import com.beaconfire.project22.customException.ProductNotInWatchlistException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ProductDao productDao;

    // Method to get orders by user
    public List<OrderDTO> getOrdersByUser(Long userId) {
        Users user = userDao.findById(userId);
       List<Order>orders = orderDao.findByUser(user);
       return orders.stream().map(this::convertToOrderDTO).collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Order not found");
        }
        return convertToOrderDTO(order);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        // Convert each OrderItem to OrderItemDTO
        List<OrderItemDTO> orderItemDTOs = order.getOrderItemList().stream()
                .map(item -> new OrderItemDTO(item.getProduct().getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        // Return the new OrderDTO
        return new OrderDTO(order.getOrderId(), order.getOrderStatus(), order.getUser().getUserId(), orderItemDTOs);
    }

    // Place an order
    @Transactional
    public Order placeOrder(Order order) throws NotEnoughInventoryException {
        List<OrderItem> orderItems = order.getOrderItemList();

        // Check each item in the order
        for (OrderItem item : orderItems) {
            Product product = productDao.findById(item.getProduct().getProductId());
            if (product == null) {
                throw new NotEnoughInventoryException("Product not found");
            }

            // Check if the product has enough stock
            if (product.getQuantity() < item.getQuantity()) {
                throw new NotEnoughInventoryException("Not enough inventory for product: " + product.getName());
            }

            // Deduct the stock from the product
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productDao.add(product);
            item.setPurchasedPrice(product.getRetailPrice());
        }

        // Save the order as "Processing"
        order.setOrderStatus("Processing");
        orderDao.add(order);
        return order;
    }

    @Transactional
    public void deleteOrder(Long orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Product not found");
        }
        orderDao.delete(order);
    }
   // Cancel an order
    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Order not found");
        }

        if ("Processing".equals(order.getOrderStatus())) {
            List<OrderItem> orderItems = order.getOrderItemList();
            for (OrderItem item : orderItems) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());  // Return the stock
                productDao.saveOrUpdate(product);
            }
            order.setOrderStatus("Canceled");
            orderDao.saveOrUpdate(order);
        } else if ("Completed".equals(order.getOrderStatus())) {
            throw new ProductNotInWatchlistException("Cannot cancel a completed order.");
        }else if ("Canceled".equals(order.getOrderStatus())){
            throw new ProductNotInWatchlistException("Cannot cancel a Canceled order.");
        }
    }


    public Order updateOrderStatus(Long orderId, String status) throws Exception {
        Order order = orderDao.findById(orderId);
        if(order == null){
            throw new Exception("Order not found");
        }
        // Update product details
        order.setOrderStatus(status);
        return orderDao.saveOrUpdate(order);  // Save the updated product
    }
}
