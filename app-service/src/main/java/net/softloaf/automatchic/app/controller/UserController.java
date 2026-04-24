package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.response.JwtResponse;
import net.softloaf.automatchic.app.dto.request.UserUpdateRequest;
import net.softloaf.automatchic.app.security.JwtUtils;
import net.softloaf.automatchic.app.security.UserDetailsImpl;
import net.softloaf.automatchic.app.security.UserDetailsServiceImpl;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.app.service.UserService;
import net.softloaf.automatchic.common.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Пользователи", description = "Управление профилем пользователя")
public class UserController {

    private final SessionService sessionService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Operation(
            summary = "Удалить пользователя по ID",
            description = "Удаляет пользователя по ID. Доступно только владельцу аккаунта.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Пользователь удалён"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав на удаление",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "403",
                                          "message": "Нет прав на удаление",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "404",
                                          "message": "Неверный ID пользователя",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(
            @PathVariable
            @Parameter(description = "ID пользователя")
            long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить свой аккаунт",
            description = "Удаляет аккаунт текущего авторизованного пользователя.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Аккаунт удалён"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизован",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "401",
                                          "message": "Неавторизованный запрос",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/self/delete")
    public ResponseEntity<?> deleteSelf() {
        userService.deleteUser(sessionService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Обновить свой профиль",
            description = "Обновляет данные текущего пользователя и возвращает новый JWT токен.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Профиль обновлён",
                            content = @Content(schema = @Schema(implementation = JwtResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизован",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "401",
                                          "message": "Неавторизованный запрос",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/self/update")
    public JwtResponse updateSelf(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новые данные пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserUpdateRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "fullName": "Иван Иванов",
                                  "group": "БИВТ-23-СП-3"
                                }
                                """
                            )
                    )
            )
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {

        userService.updateUser(userUpdateRequest);

        UserDetailsImpl userDetails =
                userDetailsService.loadUserByUsername(
                        sessionService.getCurrentUserUsername()
                );

        String token = jwtUtils.generateToken(userDetails);

        return new JwtResponse(token);
    }

    @Operation(
            summary = "Проверить подтверждение аккаунта",
            description = "Возвращает true, если email текущего пользователя подтвержден.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Статус получен",
                            content = @Content(schema = @Schema(implementation = Boolean.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизован",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "401",
                                          "message": "Неавторизованный запрос",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/self/check")
    public boolean checkConfirmedSelf() {
        return userService.checkConfirmed(sessionService.getCurrentUserId());
    }
}
