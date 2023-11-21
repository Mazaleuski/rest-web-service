package com.example.restwebservice.controllers;

import com.example.restwebservice.dto.CartDto;
import com.example.restwebservice.dto.OrderDto;
import com.example.restwebservice.dto.ProductDto;
import com.example.restwebservice.dto.UserDto;
import com.example.restwebservice.exceptions.CartIsEmptyException;
import com.example.restwebservice.services.OrderService;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@Validated
@Tag(name = "order", description = "Order Endpoints")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create order",
            description = "Create new order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order was created",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order not created - server error",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CartDto cartDto, @RequestBody @Valid UserDto userDto) throws CartIsEmptyException {
        return new ResponseEntity<>(orderService.create(userDto, cartDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update order",
            description = "Update existed order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was updated",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order not updated - server error",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public ResponseEntity<OrderDto> updateOrder(@RequestBody @Valid OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Delete order",
            description = "Delete existed order",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was deleted"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Order not deleted - server error",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteOrder(@Parameter(description = "Order id") @PathVariable @Positive int id) {
        orderService.deleteOrder(id);
    }

    @Operation(
            summary = "Find certain order",
            description = "Find certain existed order in shop by id",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order was found by id",
                    content = @Content(schema = @Schema(contentSchema = OrderDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Order not fount - forbidden operation",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@Parameter(description = "Order id") @PathVariable @Positive int id) {
        return Optional.ofNullable(orderService.getOrderById(id))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Find orders by user",
            description = "Find certain existed order in shop by user id",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders were found by user id",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Order not fount - forbidden operation",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderDto>> getOrdersByUserId(@Parameter(description = "User id") @PathVariable @Positive int id,
                                                            @Parameter(required = true, description = "Page number") @RequestParam int pageNumber,
                                                            @Parameter(required = true, description = "Item number per page") @RequestParam int pageSize,
                                                            @Parameter(required = true, description = "Search param") @RequestParam(defaultValue = "id") String param) {
        return Optional.ofNullable(orderService.getOrdersByUserId(id, pageNumber, pageSize, param))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Find products from order",
            description = "Find existed products in shop by order id",
            tags = {"order"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were found by order id",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Products not found - forbidden operation",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/products/{id}")
    public ResponseEntity<List<ProductDto>> getProductByOrderId(@Parameter(description = "Order id") @PathVariable @Positive int id) {
        return Optional.ofNullable(orderService.getProductByOrderId(id))
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(
            summary = "Save orders to file",
            description = "Save orders to .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders were saved"
            )
    })
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/download")
    public void downloadOrdersToFile(@RequestBody List<OrderDto> orders, HttpServletResponse response)
            throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
        orderService.downloadOrdersToFile(orders, response);
    }

    @Operation(
            summary = "Upload orders from file",
            description = "Upload orders from .csv file")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders were upload",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))
            )
    })

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<List<OrderDto>> uploadOrdersFromFile(@Parameter(description = "File for upload ")
                                                               @RequestParam("file") MultipartFile file) throws Exception {
        return new ResponseEntity<>(orderService.uploadOrdersFromFile(file), HttpStatus.CREATED);
    }
}
