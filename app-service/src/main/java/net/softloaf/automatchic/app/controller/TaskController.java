package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.TaskRequest;
import net.softloaf.automatchic.app.dto.request.TaskPositionRequest;
import net.softloaf.automatchic.app.dto.response.TaskBasicResponse;
import net.softloaf.automatchic.app.dto.response.TaskFullResponse;
import net.softloaf.automatchic.app.service.TaskService;
import net.softloaf.automatchic.app.service.util.SessionService;
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
                    @ApiResponse(responseCode = "200", description = "Задача успешно сохранена"),
                    @ApiResponse(responseCode = "400", description = "Достигнут лимит задач"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Задача или дисциплина не найдены")
            }
    )
    @PutMapping("/save")
    public ResponseEntity<?> saveTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные задачи",
                    required = true
            )
            @RequestBody TaskRequest taskRequest) {

        long response = taskService.save(taskRequest);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @Operation(
            summary = "Обновить позиции задач",
            description = "Массово обновляет порядок задач внутри дисциплины.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Позиции обновлены"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    @PatchMapping("/positions")
    public ResponseEntity<?> updatePositions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Список задач с новыми позициями",
                    required = true
            )
            @RequestBody List<TaskPositionRequest> positions) {

        taskService.updatePositions(positions);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задача удалена"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteTask(
            @PathVariable
            @Parameter(description = "ID задачи")
            long id) {

        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Получить запланированные задачи",
            description = "Возвращает список задач текущего пользователя с установленной датой выполнения.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список задач получен"),
                    @ApiResponse(responseCode = "401", description = "Неавторизован")
            }
    )
    @GetMapping("/self/scheduled")
    public List<TaskBasicResponse> getSelfScheduledTasks() {
        return taskService.findScheduled(sessionService.getCurrentUserId());
    }
}
