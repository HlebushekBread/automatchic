package net.softloaf.automatchic.app.service;

import net.softloaf.automatchic.app.dto.request.NewUserRequest;
import net.softloaf.automatchic.app.model.User;
import net.softloaf.automatchic.app.repository.UserRepository;
import net.softloaf.automatchic.app.service.producer.NotificationProducer;
import net.softloaf.automatchic.app.service.producer.ProgressProducer;
import net.softloaf.automatchic.app.service.token.EmailConfirmationTokenService;
import net.softloaf.automatchic.app.service.token.PasswordResetTokenService;
import net.softloaf.automatchic.app.service.util.SearchStringService;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.common.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private SessionService sessionService;
    @Mock
    private SearchStringService searchStringService;
    @Mock
    private EmailConfirmationTokenService emailConfirmationTokenService;
    @Mock
    private PasswordResetTokenService passwordResetTokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private NotificationProducer notificationProducer;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private ProgressProducer progressProducer;

    @InjectMocks
    private UserService userService;

    @Test
    void saveNewUser_shouldThrowIfUserExists() {
        NewUserRequest request = new NewUserRequest();
        request.setUsername("test@mail.com");
        request.setPassword("123123");

        when(userRepository.existsByUsername("test@mail.com"))
                .thenReturn(true);

        assertThrows(ResponseStatusException.class,
                () -> userService.saveNewUser(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteIfOwner() {
        User user = new User();
        user.setId(5L);
        user.setSubjects(List.of());

        when(userRepository.findById(5L))
                .thenReturn(Optional.of(user));

        when(sessionService.getCurrentUserId())
                .thenReturn(5L);

        userService.deleteUser(5L);

        verify(userRepository).deleteById(5L);
    }

    @Test
    void deleteUser_shouldThrowIfNotOwner() {
        User user = new User();
        user.setId(5L);
        user.setSubjects(List.of());

        when(userRepository.findById(5L))
                .thenReturn(Optional.of(user));

        when(sessionService.getCurrentUserId())
                .thenReturn(10L);

        assertThrows(ResponseStatusException.class,
                () -> userService.deleteUser(5L));

        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void confirmUser_shouldConfirm() {
        when(emailConfirmationTokenService.getEmailByToken("token"))
                .thenReturn(Optional.of("mail@test.com"));

        User user = new User();
        user.setUsername("mail@test.com");
        user.setConfirmed(false);

        when(userRepository.findByUsername("mail@test.com"))
                .thenReturn(Optional.of(user));

        userService.confirmUser("token");

        assertTrue(user.isConfirmed());

        verify(userRepository).save(user);
        verify(emailConfirmationTokenService).deleteToken("token");
    }
}
