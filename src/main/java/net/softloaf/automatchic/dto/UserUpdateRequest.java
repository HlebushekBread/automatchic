package net.softloaf.automatchic.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String fullName;
    private String group;
}
