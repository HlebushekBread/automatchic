package net.breadlab.automatchic.security;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.model.User;
import net.breadlab.automatchic.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new UserDetailsImpl(user.get());
    }
}
