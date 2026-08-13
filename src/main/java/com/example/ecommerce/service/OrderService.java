package com.example.ecommerce.service;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderRequest request);
    List<OrderDTO> getAllOrders();
    OrderDTO getOrderById(Long id);
    OrderDTO updateOrderStatus(Long id, String status);
    void deleteOrder(Long id);
}
