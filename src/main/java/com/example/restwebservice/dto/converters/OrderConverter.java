package com.example.restwebservice.dto.converters;

import com.example.restwebservice.dto.OrderDto;
import com.example.restwebservice.entities.Order;
import com.example.restwebservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class OrderConverter {

    private final ProductConverter productConverter;
    private final UserRepository userRepository;

    public OrderDto toDto(Order order) {
        return Optional.ofNullable(order).map(o -> OrderDto.builder()
                        .id(o.getId())
                        .price(o.getPrice())
                        .date(o.getDate())
                        .userId(o.getUser().getId())
                        .productList(Optional.ofNullable(o.getProductList()).map(products -> products
                                .stream().map(productConverter::toDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }

    public Order fromDto(OrderDto orderDto) {
        return Optional.ofNullable(orderDto).map(o -> Order.builder()
                        .price(o.getPrice())
                        .date(o.getDate())
                        .user(userRepository.findById(o.getUserId()).orElse(null))
                        .productList(Optional.ofNullable(o.getProductList()).map(products -> products
                                .stream().map(productConverter::fromDto).toList()).orElse(List.of()))
                        .build())
                .orElse(null);
    }
}
