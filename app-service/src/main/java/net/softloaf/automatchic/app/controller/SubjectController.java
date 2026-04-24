package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.SubjectRequest;
import net.softloaf.automatchic.app.dto.response.SubjectBasicResponse;
import net.softloaf.automatchic.app.dto.response.SubjectFullResponse;
import net.softloaf.automatchic.app.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
                    @ApiResponse(responseCode = "200", description = "Дисциплина найдена"),
                    @ApiResponse(responseCode = "403", description = "Нет доступа"),
                    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
            }
    )
    @GetMapping("/{id}/view")
    public SubjectFullResponse getSubjectViewById(
            @PathVariable
            @Parameter(description = "ID дисциплины")
            long id) {
        return subjectService.findById(false, id);
    }

    @Operation(
            summary = "Предпросмотр дисциплины",
            description = "Возвращает дисциплину для просмотра. Доступно владельцу или если дисциплина публичная.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Дисциплина найдена"),
                    @ApiResponse(responseCode = "403", description = "Нет доступа"),
                    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
            }
    )
    @GetMapping("/{id}/preview")
    public SubjectFullResponse getSubjectPreviewById(
            @PathVariable
            @Parameter(description = "ID дисциплины")
            long id) {
        return subjectService.findById(true, id);
    }

    @Operation(
            summary = "Получить публичные дисциплины",
            description = "Возвращает список публичных дисциплин с поиском, фильтрацией и пагинацией.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список получен")
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
            @RequestParam(defaultValue = "12") int size) {

        return subjectService.findPublic(query, type, page, size);
    }

    @Operation(
            summary = "Мои дисциплины",
            description = "Возвращает список дисциплин текущего пользователя.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список получен"),
                    @ApiResponse(responseCode = "401", description = "Неавторизован")
            }
    )
    @GetMapping("/self")
    public List<SubjectFullResponse> getUserSubjects() {
        return subjectService.findAllByCurrentUserId();
    }

    @Operation(
            summary = "Создать или обновить дисциплину",
            description = "Создает новую дисциплину либо обновляет существующую.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сохранено"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
                    @ApiResponse(responseCode = "403", description = "Нет прав"),
                    @ApiResponse(responseCode = "404", description = "Не найдено")
            }
    )
    @PutMapping("/save")
    public ResponseEntity<?> saveSubject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные дисциплины",
                    required = true
            )
            @RequestBody SubjectRequest subjectRequest) {

        long response = subjectService.save(subjectRequest);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @Operation(
            summary = "Скопировать дисциплину",
            description = "Создает копию существующей дисциплины.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Копия создана"),
                    @ApiResponse(responseCode = "403", description = "Нет прав"),
                    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
            }
    )
    @GetMapping("/{id}/copy")
    public ResponseEntity<?> copySubject(
            @PathVariable
            @Parameter(description = "ID исходной дисциплины")
            long id) {

        long response = subjectService.copy(id);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить дисциплину",
            description = "Удаляет дисциплину текущего пользователя.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Удалено"),
                    @ApiResponse(responseCode = "403", description = "Нет прав"),
                    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена")
            }
    )
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteOrder(
            @PathVariable
            @Parameter(description = "ID дисциплины для удаления")
            long id) {

        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
