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

    @Transactional
    public List<OrderDTO> getOrdersByUser(Long userId) {
        Users user = userDao.findById(userId);
       List<Order>orders = orderDao.findByUser(user);
       return orders.stream().map(this::convertToOrderDTO).collect(Collectors.toList());
    }


    @Transactional
    public OrderDTO getOrderById(Long orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        return convertToOrderDTO(order);
    }
    @Transactional
    public List<OrderItem> getOrderItemsByOrderId(Long orderId) {
        return orderItemDao.getAllOrderItemsByOrderId(orderId);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        // Convert each OrderItem to OrderItemDTO
        List<OrderItemDTO> orderItemDTOs = order.getOrderItemList().stream()
                .map(item -> new OrderItemDTO(item.getProduct().getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        // Return the new OrderDTO
        return new OrderDTO(order.getOrderId(), order.getOrderStatus(),
                order.getUser().getUserId(), orderItemDTOs,
                order.getDatePlaced());
    }


    @Transactional
    public List<OrderDTO> getAllOrder()  {

        List<Order>orders = orderDao.getAll();
        return orders.stream().map(this::convertToOrderDTO).collect(Collectors.toList());
    }
    // Place an order
    @Transactional
    public Order placeOrder(Order order) throws NotEnoughInventoryException {
        System.out.println("User ID: " + order.getUser().getUserId());

        Users user = userDao.findById(order.getUser().getUserId());

        if (user != null){
            order.setUser(user);
        }else {
            throw new NotEnoughInventoryException("User not found");

        }
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
            item.setOrder(order);
        }

        // Save the order as "Processing"
        order.setOrderStatus("Processing");
        orderDao.add(order);
        return order;
    }
    @Transactional
    public List<Object[]> getTop3FrequentlyPurchasedItems(Long userId) {
        return orderDao.findTop3FrequentlyPurchasedItemsByUser(userId);
    }

    @Transactional
    public List<Object[]> getTop3RecentlyPurchasedItems(Long userId) {
        return orderDao.findTop3RecentlyPurchasedItemsByUser(userId);
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


    @Transactional
    public void completeOrder(Long orderId) throws Exception {
        Order order = orderDao.findById(orderId);
        if (order == null) {
            throw new Exception("Order not found");
        }
        if ("Processing".equals(order.getOrderStatus())) {
            order.setOrderStatus("Completed");
            orderDao.saveOrUpdate(order);
        } else if ("Completed".equals(order.getOrderStatus())) {
            throw new ProductNotInWatchlistException("Cannot Complete a completed order.");
        }else if ("Canceled".equals(order.getOrderStatus())){
            throw new ProductNotInWatchlistException("Cannot Complete a Canceled order.");
        }
    }
}
