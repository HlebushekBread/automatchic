package net.softloaf.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.NewUserDto;
import net.softloaf.automatchic.model.Role;
import net.softloaf.automatchic.model.User;
import net.softloaf.automatchic.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        try {
            Map<String, String> principal = (Map<String, String>) authentication.getPrincipal();
            return Long.parseLong(principal.get("id"));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void saveNewUser(NewUserDto newUserDto) {

        User user = new User();

        if (userRepository.existsByUsername(newUserDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь уже существует");
        }

        user.setUsername(newUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        user.setFullName(newUserDto.getFullName());
        user.setGroup(newUserDto.getGroup());
        user.setRole(Role.STUDENT);

        userRepository.save(user);
    }
}
