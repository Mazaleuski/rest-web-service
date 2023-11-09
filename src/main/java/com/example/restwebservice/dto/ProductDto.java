package com.example.restwebservice.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends BaseDto {

    @CsvBindByName
    @Pattern(regexp = "[A-Za-z А-Яа-я]+", message = "Некорректное имя.")
    private String name;

    @CsvBindByName
    private String description;

    @CsvBindByName
    @NotNull
    @Digits(integer = 5, fraction = 2)
    private int price;

    @CsvBindByName
    @NotNull
    @Digits(integer = 5, fraction = 0)
    private int categoryId;

    @CsvBindByName
    @NotNull
    private String imagePath;
}
