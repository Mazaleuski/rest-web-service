package com.example.restwebservice.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseDto {

    @NotNull
    @Digits(integer = 5, fraction = 0)
    @CsvBindByName
    protected int id;
}
