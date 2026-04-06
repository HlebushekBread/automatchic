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
    private final RegistrationTokenService tokenService;
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

        userRepository.save(user);

        sendConfirmationEmail(user.getId());
    }

    @Transactional(readOnly = true)
    public void sendConfirmationEmail(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID"));

        notificationProducer.sendRegistrationEmail(user.getUsername(), tokenService.generateToken(user.getUsername()));
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

    @Transactional
    public void enableUser(String token) {
        String username = tokenService.getEmailByToken(token).orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "Невалидный токен"));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный Username"));
        user.setEnabled(true);
        userRepository.save(user);

        tokenService.deleteToken(token);
    }

    @Transactional(readOnly = true)
    public boolean checkEnabled(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID"));

        return user.isEnabled();
    }
}
