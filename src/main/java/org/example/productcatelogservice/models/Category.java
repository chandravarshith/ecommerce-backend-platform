package org.example.productcatelogservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Category extends BaseClass {
    private String name;
    private String description;
    private List<Product> products;
}
