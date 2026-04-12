package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.NewUserRequest;
import net.softloaf.automatchic.app.dto.request.UserUpdateRequest;
import net.softloaf.automatchic.app.model.Role;
import net.softloaf.automatchic.app.model.User;
import net.softloaf.automatchic.app.repository.UserRepository;
import net.softloaf.automatchic.app.service.producer.NotificationProducer;
import net.softloaf.automatchic.app.service.token.EmailConfirmationTokenService;
import net.softloaf.automatchic.app.service.token.PasswordResetTokenService;
import net.softloaf.automatchic.app.service.util.SearchStringService;
import net.softloaf.automatchic.app.service.util.SessionService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class UserService {
    private final SessionService sessionService;
    private final SearchStringService searchStringService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationProducer notificationProducer;
    private final CacheManager cacheManager;

    @Transactional
    public void saveNewUser(NewUserRequest newUserRequest) {
        if(newUserRequest.getUsername() == null || newUserRequest.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Логин и пароль не могут быть null");
        }

        if(newUserRequest.getUsername().isEmpty() || newUserRequest.getPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Логин и пароль не могут быть пустыми");
        }

        User user = new User();

        if (userRepository.existsByUsername(newUserRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь уже существует");
        }

        user.setUsername(newUserRequest.getUsername());
        user.setPassword(passwordEncoder.encode(newUserRequest.getPassword()));
        user.setFullName(newUserRequest.getFullName());
        user.setGroup(newUserRequest.getGroup());
        user.setRole(Role.STUDENT);

        user.setConfirmed(false);

        user.setSearchString(searchStringService.getSearchString(user));

        userRepository.save(user);

        sendConfirmationEmail(user.getId());
    }

    @Transactional(readOnly = true)
    public void sendConfirmationEmail(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID"));
        if(user.isConfirmed()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь уже подтвержден");
        }
        notificationProducer.sendEmailConfirmationEmail(user.getUsername(), emailConfirmationTokenService.generateToken(user.getUsername()));
    }

    @Transactional(readOnly = true)
    public void sendPasswordResetEmail(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID"));

        notificationProducer.sendPasswordResetEmail(user.getUsername(), passwordResetTokenService.generateToken(user.getUsername()));
    }

    @Transactional
    public void deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID"));

        if (user.getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        userRepository.deleteById(id);

        Cache cache = cacheManager.getCache("user_details");
        if (cache != null) {
            cache.evict(user.getUsername());
        }
    }

    @Transactional
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(sessionService.getCurrentUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неавторизованный запрос"));

        user.setFullName(userUpdateRequest.getFullName());
        user.setGroup(userUpdateRequest.getGroup());

        user.setSearchString(searchStringService.getSearchString(user));

        userRepository.save(user);

        Cache cache = cacheManager.getCache("user_details");
        if (cache != null) {
            cache.evict(user.getUsername());
        }
    }

    @Transactional(readOnly = true)
    public boolean checkConfirmed(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID"));

        return user.isConfirmed();
    }

    @Transactional
    public void confirmUser(String token) {
        String username = emailConfirmationTokenService.getEmailByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "Невалидный токен"));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Username"));
        user.setConfirmed(true);
        userRepository.save(user);

        emailConfirmationTokenService.deleteToken(token);

        Cache cache = cacheManager.getCache("user_details");
        if (cache != null) {
            cache.evict(username);
        }
    }

    @Transactional
    public void resetPassword(String token, String password) {
        String username = passwordResetTokenService.getEmailByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "Невалидный токен"));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Username"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordResetTokenService.deleteToken(token);

        Cache cache = cacheManager.getCache("user_details");
        if (cache != null) {
            cache.evict(username);
        }
    }
}