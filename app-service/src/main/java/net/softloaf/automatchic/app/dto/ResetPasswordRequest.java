package net.softloaf.automatchic.app.dto;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String password;
}