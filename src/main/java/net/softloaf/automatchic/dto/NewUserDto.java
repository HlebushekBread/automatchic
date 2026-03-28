package net.softloaf.automatchic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NewUserDto {
    private String username;
    private String password;
    private String fullName;
    private String group;
}
