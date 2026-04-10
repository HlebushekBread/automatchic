package net.softloaf.automatchic.app.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.Data;
import net.softloaf.automatchic.app.model.User;

@Data
public class UserBasicResponse {
    private String username;
    private String fullName;
    private String group;
    private String role;

    public UserBasicResponse(User user) {
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.group = user.getGroup();
        this.role = user.getRole().toString();
    }
}
