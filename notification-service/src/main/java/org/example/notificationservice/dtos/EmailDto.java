package org.example.notificationservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailDto implements Serializable {
    private String sender;
    private String recipient;
    private String subject;
    private String message;
}
