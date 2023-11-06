package com.example.restwebservice.dto;

import com.example.restwebservice.csv.ProductDtoCsvConverter;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
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

    @CsvBindByName
    @Digits(integer = 5, fraction = 2)
    private int price;

    @CsvDate("yyyy-MM-dd")
    @CsvBindByName
    @Past
    private LocalDate date;

    @CsvBindByName
    @NotNull
    private int userId;

    @CsvBindAndSplitByName(elementType = ProductDto.class, splitOn = "\\|", converter = ProductDtoCsvConverter.class)
    @NotNull
    private List<ProductDto> productList;
}
