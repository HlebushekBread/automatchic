package net.softloaf.automatchic.app.dto.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private String username;
    private String password;
    private String fullName;
    private String group;
}
