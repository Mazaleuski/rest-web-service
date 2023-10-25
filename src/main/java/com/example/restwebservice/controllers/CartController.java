package com.example.restwebservice.controllers;

import com.example.restwebservice.dto.CartDto;
import com.example.restwebservice.services.CartService;
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
public class CartController {

    private final CartService cartService;

    @PostMapping("/addProduct/{id}")
    public ResponseEntity<CartDto> addProduct(@PathVariable @Positive int id, @RequestBody CartDto cartDto) {
        return new ResponseEntity<>(cartService.addProduct(id, cartDto), HttpStatus.OK);
    }

    @DeleteMapping("/removeProduct/{id}")
    public ResponseEntity<CartDto> removeProduct(@PathVariable @Positive int id, @RequestBody CartDto cartDto) {
        return new ResponseEntity<>(cartService.removeProduct(id, cartDto), HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CartDto> clear(@RequestBody CartDto cartDto) {
        return new ResponseEntity<>(cartService.clear(cartDto), HttpStatus.OK);
    }
}
