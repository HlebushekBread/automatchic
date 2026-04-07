package net.softloaf.automatchic.app.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.UserDto;
import net.softloaf.automatchic.app.model.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UserDto userDto;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userDto.getRole().toString()));
        return grantedAuthorities;
    }

    @JsonIgnore
    @Override
    public @Nullable String getPassword() {
        return userDto.getPassword();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return userDto.getUsername();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
