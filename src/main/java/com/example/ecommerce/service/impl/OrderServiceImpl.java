package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.CreateOrderRequest;
import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.dto.OrderItemDTO.OrderItemDTOBuilder;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderDTO toDTO(Order order){
        List<com.example.ecommerce.dto.OrderItemDTO> items = order.getItems().stream().map(i -> com.example.ecommerce.dto.OrderItemDTO.builder()
                .id(i.getId())
                .productId(i.getProduct().getId())
                .productName(i.getProduct().getName())
                .quantity(i.getQuantity())
                .price(i.getPrice())
                .build()).collect(Collectors.toList());

        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        logger.info("Creating order for user {}", request.getUserId());
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Order order = Order.builder().user(user).build();

        BigDecimal total = BigDecimal.ZERO;

        for (com.example.ecommerce.dto.OrderItemDTO itemDTO : request.getItems()){
            Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProductId()));
            if(product.getQuantity() < itemDTO.getQuantity()){
                throw new IllegalArgumentException("Product " + product.getId() + " does not have enough quantity");
            }
            BigDecimal itemPrice = product.getPrice();
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .price(itemPrice)
                    .build();
            order.getItems().add(item);
            total = total.add(itemPrice.multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            // reduce product quantity
            product.setQuantity(product.getQuantity() - itemDTO.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        // ensure orderItems have ids (cascade should have persisted them)
        return toDTO(saved);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        return toDTO(order);
    }

    @Override
    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        try{
            OrderStatus s = OrderStatus.valueOf(status);
            order.setOrderStatus(s);
            Order updated = orderRepository.save(order);
            logger.info("Updated order {} status to {}", id, status);
            return toDTO(updated);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        orderRepository.delete(order);
        logger.info("Deleted order {}", id);
    }
}
