package com.example.ecommerce.dto;

import com.example.ecommerce.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusRequest {
    @NotNull(message = "orderStatus is required")
    private OrderStatus orderStatus;
}
