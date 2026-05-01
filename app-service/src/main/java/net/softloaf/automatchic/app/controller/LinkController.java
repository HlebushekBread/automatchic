package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.LinkRequest;
import net.softloaf.automatchic.app.dto.response.IdResponse;
import net.softloaf.automatchic.app.service.LinkService;
import net.softloaf.automatchic.common.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/links")
@Tag(name = "Ссылки", description = "Управление полезными ссылками внутри дисциплин")
public class LinkController {

    private final LinkService linkService;

    @Operation(
            summary = "Создать ссылку",
            description = "Создает новую ссылку для дисциплины.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ссылка успешно сохранена",
                            content = @Content(schema = @Schema(implementation = IdResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Достигнут лимит ссылок",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 400,
                                          "message": "Достигнут лимит ссылок",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 403,
                                          "message": "Нет прав на создание",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Дисциплина не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 404,
                                          "message": "Неверный ID дисциплины",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/new/{subjectId}")
    public IdResponse createLink(
            @Parameter(description = "ID дисциплины")
            @PathVariable long subjectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные ссылки",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LinkRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "name": "Telegram чат",
                                  "fullLink": "https://t.me/example"
                                }
                                """
                            )
                    )
            )
            @RequestBody LinkRequest linkRequest
    ) {
        long response = linkService.create(subjectId, linkRequest);
        return new IdResponse(response);
    }

    @Operation(
            summary = "Обновить ссылку",
            description = "Обновляет существующую ссылку.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Ссылка успешно сохранена",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 403,
                                          "message": "Нет прав на редактирование",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ссылка не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 404,
                                          "message": "Неверный ID ссылки",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateLink(
            @Parameter(description = "ID ссылки")
            @PathVariable long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные ссылки",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LinkRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "name": "Telegram чат",
                                  "fullLink": "https://t.me/example"
                                }
                                """
                            )
                    )
            )
            @RequestBody LinkRequest linkRequest
    ) {
        linkService.update(id, linkRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить ссылку",
            description = "Удаляет ссылку по ID. Доступно только владельцу дисциплины.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Ссылка удалена"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 403,
                                          "message": "Нет прав на удаление",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ссылка не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 404,
                                          "message": "Неверный ID ссылки",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable
            @Parameter(description = "ID ссылки")
            long id
    ) {
        linkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
