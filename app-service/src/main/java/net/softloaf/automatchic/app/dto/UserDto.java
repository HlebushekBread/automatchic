package net.softloaf.automatchic.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.softloaf.automatchic.app.model.Role;
import net.softloaf.automatchic.app.model.User;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String group;
    private Role role;
    private Boolean isConfirmed;
    private LocalDateTime registeredAt;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.fullName = user.getFullName();
        this.group = user.getGroup();
        this.role = user.getRole();
        this.isConfirmed = user.isConfirmed();
        this.registeredAt = user.getRegisteredAt();
    }
}
