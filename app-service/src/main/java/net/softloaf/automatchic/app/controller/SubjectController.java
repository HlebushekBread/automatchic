package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.SubjectRequest;
import net.softloaf.automatchic.app.dto.response.IdResponse;
import net.softloaf.automatchic.app.dto.response.SubjectBasicResponse;
import net.softloaf.automatchic.app.dto.response.SubjectFullResponse;
import net.softloaf.automatchic.app.service.SubjectService;
import net.softloaf.automatchic.common.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
@Tag(name = "Дисциплины", description = "Управление учебными дисциплинами и их просмотр")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(
            summary = "Получить дисциплину полностью",
            description = "Возвращает полную информацию о дисциплине владельцу.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дисциплина найдена",
                            content = @Content(schema = @Schema(implementation = SubjectFullResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 403,
                                          "message": "Доступ запрещен",
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
    @GetMapping("/{id}/view")
    public SubjectFullResponse getSubjectViewById(
            @Parameter(description = "ID дисциплины")
            @PathVariable
            long id
    ) {
        return subjectService.findById(false, id);
    }

    @Operation(
            summary = "Предпросмотр дисциплины",
            description = "Возвращает дисциплину для просмотра. Доступно владельцу или если дисциплина публичная.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дисциплина найдена",
                            content = @Content(schema = @Schema(implementation = SubjectFullResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизованный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 403,
                                          "message": "Нет прав на предпросмотр",
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
    @GetMapping("/{id}/preview")
    public SubjectFullResponse getSubjectPreviewById(
            @Parameter(description = "ID дисциплины")
            @PathVariable
            long id
    ) {
        return subjectService.findById(true, id);
    }

    @Operation(
            summary = "Получить публичные дисциплины",
            description = "Возвращает список публичных дисциплин с поиском, фильтрацией и пагинацией.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список получен",
                            content = @Content(schema = @Schema(implementation = SubjectBasicResponse.class))
                    )
            }
    )
    @GetMapping("/public")
    public List<SubjectBasicResponse> getPublicSubjects(
            @Parameter(description = "Поисковый запрос")
            @RequestParam(defaultValue = "") String query,

            @Parameter(description = "Тип оценивания: all, exam, credit и т.д.")
            @RequestParam(defaultValue = "all") String type,

            @Parameter(description = "Номер страницы, начиная с 0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "12") int size
    ) {
        return subjectService.findPublic(query, type, page, size);
    }

    @Operation(
            summary = "Мои дисциплины",
            description = "Возвращает список дисциплин текущего пользователя.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список получен",
                            content = @Content(schema = @Schema(implementation = SubjectFullResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизован",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 401,
                                          "message": "Неавторизованный запрос",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/self")
    public List<SubjectFullResponse> getUserSubjects() {
        return subjectService.findAllByCurrentUserId();
    }

    @Operation(
            summary = "Создать дисциплину",
            description = "Создает новую дисциплину.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сохранено",
                            content = @Content(schema = @Schema(implementation = IdResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Ошибка валидации",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 400,
                                          "message": "Достигнут лимит дисциплин",
                                          "timestamp": 0
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизованный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    )
            }
    )
    @PostMapping("/new")
    public IdResponse createSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные дисциплины",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SubjectRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "name": "Математический анализ",
                                  "teacher": "Иванов И.И.",
                                  "description": "Основной курс",
                                  "gradingType": "EXAM",
                                  "evaluationType": "TOTAL",
                                  "targetGrade": 3,
                                  "gradingMax": 100,
                                  "grading5": 85,
                                  "grading4": 70,
                                  "grading3": 50,
                                  "gradingMin": 0,
                                  "publicity": "PRIVATE"
                                }
                                """
                            )
                    )
            )
            @RequestBody SubjectRequest subjectRequest
    ) {
        long response = subjectService.create(subjectRequest);
        return new IdResponse(response);
    }

    @Operation(
            summary = "Обновить дисциплину",
            description = "Обновляет существующую дисциплину.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Сохранено",
                            content = @Content(schema = @Schema(implementation = Void.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизованный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав",
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
                            description = "Не найдено",
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
    @PutMapping("/{id}/update")
    public ResponseEntity<?> saveSubject(
            @Parameter(description = "ID исходной дисциплины")
            @PathVariable long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные дисциплины",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SubjectRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "name": "Математический анализ",
                                  "teacher": "Иванов И.И.",
                                  "description": "Основной курс",
                                  "gradingType": "EXAM",
                                  "evaluationType": "TOTAL",
                                  "targetGrade": 3,
                                  "gradingMax": 100,
                                  "grading5": 85,
                                  "grading4": 70,
                                  "grading3": 50,
                                  "gradingMin": 0,
                                  "publicity": "PRIVATE"
                                }
                                """
                            )
                    )
            )
            @RequestBody SubjectRequest subjectRequest
    ) {
        subjectService.update(id, subjectRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Скопировать дисциплину",
            description = "Создает копию существующей дисциплины.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Копия создана",
                            content = @Content(schema = @Schema(implementation = IdResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизованный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": 403,
                                          "message": "Нет прав на копирование",
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
    @GetMapping("/{id}/copy")
    public IdResponse copySubject(
            @Parameter(description = "ID исходной дисциплины")
            @PathVariable
            long id
    ) {
        long response = subjectService.copy(id);
        return new IdResponse(response);
    }

    @Operation(
            summary = "Удалить дисциплину",
            description = "Удаляет дисциплину текущего пользователя.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Удалено"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неавторизованный запрос",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = Void.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав",
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
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteOrder(
            @Parameter(description = "ID дисциплины для удаления")
            @PathVariable
            long id
    ) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
