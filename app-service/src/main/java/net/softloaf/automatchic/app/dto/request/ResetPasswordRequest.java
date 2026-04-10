package net.softloaf.automatchic.app.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String password;
}
