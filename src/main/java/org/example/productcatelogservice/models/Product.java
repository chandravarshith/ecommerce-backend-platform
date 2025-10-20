package org.example.productcatelogservice.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product extends BaseClass {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Category category;

    // Any Business fields
    private Boolean isPrime;
}
