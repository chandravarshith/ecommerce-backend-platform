package org.example.productcatelogservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
public class Product extends BaseClass {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Category category;

    // Any Business fields
    private Boolean isPrime;

    public Product() {
        this.setCreationAt(new Date());
        this.setLastUpdateAt(new Date());
        this.setState(State.ACTIVE);
    }
}
