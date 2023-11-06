package com.example.restwebservice.services;

import com.example.restwebservice.dto.CartDto;
import com.example.restwebservice.dto.OrderDto;
import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.dto.converters.OrderConverter;
import com.example.restwebservice.dto.converters.ProductConverter;
import com.example.restwebservice.dto.converters.UserConverter;
import com.example.restwebservice.entities.Order;
import com.example.restwebservice.entities.User;
import com.example.restwebservice.exceptions.CartIsEmptyException;
import com.example.restwebservice.repositories.OrderRepository;
import com.example.restwebservice.repositories.UserRepository;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@AllArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final ProductConverter productConverter;
    private final OrderConverter orderConverter;
    private final OrderRepository orderRepository;

    public OrderDto create(UserDto userDto, CartDto cartDto) throws CartIsEmptyException {
        if (cartDto.getProducts() == null) {
            throw new CartIsEmptyException("Cart is empty");
        }
        Order order = Order.builder()
                .price(cartDto.getTotalPrice())
                .date(LocalDate.now())
                .user(userConverter.fromDto(userDto))
                .productList(cartDto.getProducts().stream().map(productConverter::fromDto).toList())
                .build();
        order = orderRepository.createOrUpdateOrder(order);
        OrderDto orderDto = orderConverter.toDto(order);
        userDto.getOrders().add(orderDto);
        userRepository.createOrUpdateUser(userConverter.fromDto(userDto));
        cartDto.clear();
        cartDto.setTotalPrice(0);
        return orderDto;
    }

    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = Optional.ofNullable(orderRepository.findById(orderDto.getId()))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with id %d not found", orderDto.getId())));
        User user = userRepository.findById(orderDto.getUserId());
        order.setUser(user);
        order.setProductList(orderDto.getProductList().stream().map(productConverter::fromDto).toList());
        order.setPrice(order.getPrice());
        orderRepository.createOrUpdateOrder(order);
        return orderDto;
    }

    public void deleteOrder(int id) {
        orderRepository.delete(id);
    }

    public OrderDto getOrderById(int id) {
        Order order = Optional.ofNullable(orderRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with id %d not found", id)));
        return orderConverter.toDto(order);
    }

    public List<OrderDto> getOrdersByUserId(int id) {
        if (userRepository.findById(id) == null) {
            throw new EntityNotFoundException(String.format("User with id %d not found", id));
        }
        List<OrderDto> list = orderRepository.findByUserId(id).stream().map(orderConverter::toDto).toList();
        if (list.size() == 0) {
            throw new EntityNotFoundException(String.format("User with id %d dont have orders", id));
        }
        return list;
    }

    public List<ProductDto> getProductByOrderId(int id) {
        Order order = Optional.ofNullable(orderRepository.findById(id))
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order with id %d not found", id)));
        return order.getProductList().stream().map(productConverter::toDto).toList();
    }

    public void downloadOrdersToFile(List<OrderDto> orders, String path)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        int userId = orders.get(0).getUserId();
        try (Writer writer = Files.newBufferedWriter(Path.of(path + String.format("/userId- %d - orders.csv", userId)))) {
            StatefulBeanToCsv<OrderDto> beanToCsv = new StatefulBeanToCsvBuilder<OrderDto>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(',')
                    .build();
            beanToCsv.write(orders);
        }
    }

    public List<OrderDto> uploadOrdersFromFile(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<OrderDto> csvToBean = new CsvToBeanBuilder<OrderDto>(reader)
                    .withType(OrderDto.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreQuotations(true)
                    .withSeparator(',')
                    .build();
            List<OrderDto> ordersDtoList = new ArrayList<>();
            List<Order> orders = new ArrayList<>();
            csvToBean.forEach(ordersDtoList::add);
            for (OrderDto dto : ordersDtoList) {
                Order o = orderRepository.createOrUpdateOrder(orderConverter.fromDto(dto));
                orders.add(o);
            }
            return orders.stream().map(orderConverter::toDto).toList();
        }
    }
}
