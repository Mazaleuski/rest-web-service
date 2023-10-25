package com.example.restwebservice.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends BaseDto {

    @Digits(integer = 5, fraction = 2)
    private int price;

    @Past
    private LocalDate date;

    @NotNull
    private int userId;

    @NotNull
    private List<ProductDto> productList;
}
