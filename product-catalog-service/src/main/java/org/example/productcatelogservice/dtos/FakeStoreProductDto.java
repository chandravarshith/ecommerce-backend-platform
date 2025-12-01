package org.example.productcatelogservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeStoreProductDto {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String image;
    private Double price;
}
