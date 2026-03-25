package net.breadlab.automatchic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserDto {
    private String username;
    private String password;
    private String fullName;
    private String group;
}
