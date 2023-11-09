package com.example.restwebservice.controllers;

import com.example.restwebservice.dto.CartDto;
import com.example.restwebservice.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
@Validated
@Tag(name = "cart", description = "Cart Endpoints")

public class CartController {

    private final CartService cartService;

    @Operation(
            summary = "Add product",
            description = "Add product to cart",
            tags = {"cart"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was added",
                    content = @Content(schema = @Schema(contentSchema = CartDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @PostMapping("/addProduct/{id}")
    public ResponseEntity<CartDto> addProduct(@Parameter(description = "Product id") @PathVariable @Positive int id, @RequestBody CartDto cartDto) {
        return new ResponseEntity<>(cartService.addProduct(id, cartDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Remove product",
            description = "Remove product from cart",
            tags = {"cart"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was removed",
                    content = @Content(schema = @Schema(contentSchema = CartDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(schema = @Schema(implementation = String.class))
            )
    })

    @DeleteMapping("/removeProduct/{id}")
    public ResponseEntity<CartDto> removeProduct(@Parameter(description = "Product id") @PathVariable @Positive int id, @RequestBody CartDto cartDto) {
        return new ResponseEntity<>(cartService.removeProduct(id, cartDto), HttpStatus.OK);
    }

    @Operation(
            summary = "Clear cart",
            description = "Remove all products from cart")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products were removed from cart",
                    content = @Content(schema = @Schema(implementation = CartDto.class))
            )
    })

    @DeleteMapping("/clear")
    public ResponseEntity<CartDto> clear(@RequestBody CartDto cartDto) {
        return new ResponseEntity<>(cartService.clear(cartDto), HttpStatus.OK);
    }
}
