package net.softloaf.automatchic.app.security;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.app.dto.UserDto;
import net.softloaf.automatchic.app.model.User;
import net.softloaf.automatchic.app.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Cacheable(value = "user_details", key = "#username")
    @Override
    public UserDetailsImpl loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        //log.info("Cache miss. Загрузка из БД: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя не существует"));
        return new UserDetailsImpl(new UserDto(user));
    }
}
