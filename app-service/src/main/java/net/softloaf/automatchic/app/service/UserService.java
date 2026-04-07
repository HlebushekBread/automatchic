package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.NewUserRequest;
import net.softloaf.automatchic.app.dto.UserUpdateRequest;
import net.softloaf.automatchic.app.model.Role;
import net.softloaf.automatchic.app.model.User;
import net.softloaf.automatchic.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class UserService {
    private final SessionService sessionService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationProducer notificationProducer;

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
    }

    @Transactional
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(sessionService.getCurrentUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неавторизованный запрос"));

        user.setFullName(userUpdateRequest.getFullName());
        user.setGroup(userUpdateRequest.getGroup());

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean checkEnabled(long id) {
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
    }

    @Transactional
    public void resetPassword(String token, String password) {
        String username = passwordResetTokenService.getEmailByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "Невалидный токен"));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Username"));
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        passwordResetTokenService.deleteToken(token);
    }
}
