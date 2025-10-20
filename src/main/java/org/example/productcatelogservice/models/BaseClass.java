package org.example.productcatelogservice.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class BaseClass {
    private Long id;
    private Date creationAt;
    private Date lastUpdateAt;
    private State state;
}
