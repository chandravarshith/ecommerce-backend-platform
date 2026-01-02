package org.example.userauthenticationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
    private String sender;
    private String recipient;
    private String subject;
    private String message;
}
