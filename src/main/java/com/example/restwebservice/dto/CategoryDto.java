package com.example.restwebservice.dto;

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

    @Pattern(regexp = "[A-Za-z А-Яа-я]+", message = "Некорректное имя.")
    private String name;

    private int rating;
    private String imagePath;
    private List<ProductDto> products;
}
