package com.example.restwebservice.dto;

import com.example.restwebservice.csv.ProductDtoCsvConverter;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto extends BaseDto {

    @CsvBindByName
    @Pattern(regexp = "[A-Za-z А-Яа-я]+", message = "Некорректное имя.")
    private String name;

    @CsvBindByName
    private int rating;

    @CsvBindByName
    private String imagePath;

    @CsvBindAndSplitByName(elementType= ProductDto.class, splitOn = "\\|", converter = ProductDtoCsvConverter.class)
    private List<ProductDto> products;
}
