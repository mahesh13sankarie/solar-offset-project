package org.example.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElectricityMapCredentialDTO {
    private String countryCode;
    private String token;
}