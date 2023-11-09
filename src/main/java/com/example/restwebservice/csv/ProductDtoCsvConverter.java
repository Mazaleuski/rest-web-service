package com.example.restwebservice.csv;

import com.example.restwebservice.dto.ProductDto;
import com.opencsv.bean.AbstractCsvConverter;

public class ProductDtoCsvConverter extends AbstractCsvConverter {
    @Override
    public Object convertToRead(String value) {
        String[] split = value.split("\\*", 6);
        return ProductDto.builder()
                .id(Integer.parseInt(split[0]))
                .name(split[1])
                .description(split[2])
                .price(Integer.parseInt(split[3]))
                .categoryId(Integer.parseInt(split[4]))
                .imagePath(split[5])
                .build();
    }

    @Override
    public String convertToWrite(Object value) {
        ProductDto p = (ProductDto) value;
        return String.format("%d*%s*%s*%d*%d*%s", p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getCategoryId(), p.getImagePath());
    }
}
