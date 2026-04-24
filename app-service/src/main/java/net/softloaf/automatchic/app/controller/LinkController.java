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
            summary = "Создать или обновить ссылку",
            description = "Создает новую ссылку для дисциплины либо обновляет существующую.",
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
                                          "status": "400",
                                          "message": "Достигнут лимит ссылок",
                                          "timestamp": "0"
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
                            description = "Ссылка или дисциплина не найдены",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "404",
                                          "message": "Неверный ID ссылки",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @PutMapping("/save")
    public IdResponse saveTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные ссылки",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LinkRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "id": 0,
                                  "name": "Telegram чат",
                                  "fullLink": "https://t.me/example",
                                  "subjectId": 15
                                }
                                """
                            )
                    )
            )
            @RequestBody LinkRequest linkRequest
    ) {
        long response = linkService.save(linkRequest);
        return new IdResponse(response);
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
                            description = "Ссылка не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "404",
                                          "message": "Неверный ID ссылки",
                                          "timestamp": "0"
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
