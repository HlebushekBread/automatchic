package net.softloaf.automatchic.app.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.AuthRequest;
import net.softloaf.automatchic.app.dto.request.NewUserRequest;
import net.softloaf.automatchic.app.dto.request.ResetPasswordRequest;
import net.softloaf.automatchic.app.dto.response.JwtResponse;
import net.softloaf.automatchic.app.security.JwtUtils;
import net.softloaf.automatchic.app.security.UserDetailsImpl;
import net.softloaf.automatchic.app.security.UserDetailsServiceImpl;
import net.softloaf.automatchic.app.service.UserService;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.common.metrics.Metrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Авторизация", description = "Управления авторизацией, подтверждениями и паролем")
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MeterRegistry meterRegistry;

    @Operation(
            summary = "Авторизация пользователя",
            description = "Проверяет логин и пароль пользователя, возвращает JWT токен.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная авторизация",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Неверные данные пользователя")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Логин и пароль пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "username": "user@mail.com",
                              "password": "123456"
                            }
                            """)
                    )
            )
            @RequestBody AuthRequest authRequest
    ) {
        UserDetailsImpl userDetails;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные данные пользователя");
        }

        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя и сразу возвращает JWT токен.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно зарегистрирован",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Пользователь уже существует"),
                    @ApiResponse(responseCode = "422", description = "Логин или пароль null")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> saveNewUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NewUserRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "username": "user@mail.com",
                              "password": "123456",
                              "fullName": "Иван Иванов",
                              "group": "БИВТ-23-СП-3"
                            }
                            """)
                    )
            )
            @RequestBody NewUserRequest newUserRequest
    ) {
        userService.saveNewUser(newUserRequest);
        meterRegistry.counter(Metrics.USERS_REGISTERED_TOTAL).increment();

        UserDetailsImpl userDetails;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            newUserRequest.getUsername(),
                            newUserRequest.getPassword()
                    )
            );
            userDetails = userDetailsService.loadUserByUsername(newUserRequest.getUsername());
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверные данные пользователя");
        }

        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Operation(
            summary = "Повторная отправка письма подтверждения",
            description = "Отправляет письмо подтверждения текущему авторизованному пользователю.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Письмо отправлено"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @GetMapping("/resend")
    public ResponseEntity<?> resendConfirm() {
        userService.sendConfirmationEmail(sessionService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Подтверждение email",
            description = "Подтверждает аккаунт пользователя по токену.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Email подтвержден"),
                    @ApiResponse(responseCode = "410", description = "Токен недействителен")
            }
    )
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmEmail(
            @Parameter(description = "Токен подтверждения")
            @RequestBody String token
    ) {
        userService.confirmUser(token);
        meterRegistry.counter(Metrics.USERS_CONFIRMED_TOTAL).increment();
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Запрос на восстановление пароля",
            description = "Отправляет письмо со ссылкой для сброса пароля.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Письмо отправлено"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Parameter(description = "Email пользователя")
            @RequestBody String username
    ) {
        userService.sendPasswordResetEmail(username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Сброс пароля",
            description = "Устанавливает новый пароль по токену восстановления.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пароль успешно изменен"),
                    @ApiResponse(responseCode = "410", description = "Токен недействителен")
            }
    )
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Токен и новый пароль",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ResetPasswordRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "token": "16763be4-6022-406e-a950-fcd5018633ca",
                              "password": "newPassword123"
                            }
                            """)
                    )
            )
            @RequestBody ResetPasswordRequest resetPasswordRequest
    ) {
        userService.resetPassword(
                resetPasswordRequest.getToken(),
                resetPasswordRequest.getPassword()
        );
        return ResponseEntity.noContent().build();
    }
}