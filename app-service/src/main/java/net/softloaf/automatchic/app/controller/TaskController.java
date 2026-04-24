package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.TaskRequest;
import net.softloaf.automatchic.app.dto.request.TaskPositionRequest;
import net.softloaf.automatchic.app.dto.response.IdResponse;
import net.softloaf.automatchic.app.dto.response.TaskBasicResponse;
import net.softloaf.automatchic.app.dto.response.TaskFullResponse;
import net.softloaf.automatchic.app.service.TaskService;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.common.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
@Tag(name = "Задачи", description = "Управление задачами внутри дисциплин")
public class TaskController {

    private final TaskService taskService;
    private final SessionService sessionService;

    @Operation(
            summary = "Создать или обновить задачу",
            description = "Создает новую задачу либо обновляет существующую.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Задача успешно сохранена",
                            content = @Content(schema = @Schema(implementation = IdResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Достигнут лимит задач",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "400",
                                          "message": "Достигнут лимит задач",
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
                                          "message": "Нет прав на редактирование",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача или дисциплина не найдены",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "404",
                                          "message": "Неверный ID задачи",
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
                    description = "Данные задачи",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TaskRequest.class),
                            examples = @ExampleObject(value = """
                                {
                                  "id": 0,
                                  "name": "Контрольная работа",
                                  "type": "EXAM",
                                  "dueDate": "2026-05-10T12:00:00",
                                  "maxGrade": 100,
                                  "receivedGrade": 0,
                                  "gradeWeight": 1.0,
                                  "position": 1,
                                  "subjectId": 15
                                }
                                """
                            )
                    )
            )
            @RequestBody TaskRequest taskRequest
    ) {
        long response = taskService.save(taskRequest);
        return new IdResponse(response);
    }

    @Operation(
            summary = "Обновить позиции задач",
            description = "Массово обновляет порядок задач внутри дисциплины.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Позиции обновлены"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет прав доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "403",
                                          "message": "Нет прав на редактирование",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Задача не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "404",
                                          "message": "Неверный ID задачи",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/positions")
    public ResponseEntity<?> updatePositions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Список задач с новыми позициями",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TaskPositionRequest.class),
                            examples = @ExampleObject(value = """
                                [
                                  {
                                    "id": 12,
                                    "position": 1
                                  },
                                  {
                                    "id": 13,
                                    "position": 2
                                  }
                                ]
                                """
                            )
                    )
            )
            @RequestBody List<TaskPositionRequest> positions
    ) {
        taskService.updatePositions(positions);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Задача удалена"
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
                            description = "Задача не найдена",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "404",
                                          "message": "Неверный ID задачи",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteTask(
            @PathVariable
            @Parameter(description = "ID задачи")
            long id
    ) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить запланированные задачи",
            description = "Возвращает список задач текущего пользователя с установленной датой выполнения.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список задач получен",
                            content = @Content(schema = @Schema(implementation = TaskBasicResponse.class))
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
    @GetMapping("/self/scheduled")
    public List<TaskBasicResponse> getSelfScheduledTasks() {
        return taskService.findScheduled(sessionService.getCurrentUserId());
    }
}