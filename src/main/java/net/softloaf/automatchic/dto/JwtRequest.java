package net.softloaf.automatchic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
