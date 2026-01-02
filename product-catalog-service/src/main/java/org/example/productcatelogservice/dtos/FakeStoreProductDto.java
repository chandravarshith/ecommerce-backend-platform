package org.example.productcatelogservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FakeStoreProductDto implements Serializable {
    private Long id;
    private String category;
    private String title;
    private String description;
    private String image;
    private Double price;
}
