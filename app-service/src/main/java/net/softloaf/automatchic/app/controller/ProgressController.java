package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.service.ProgressService;
import net.softloaf.automatchic.common.dto.response.ProgressChartDataResponse;
import net.softloaf.automatchic.common.dto.response.ProgressSnapshotResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/progress")
@Tag(name = "Прогресс", description = "История и аналитика прогресса по дисциплинам")
public class ProgressController {

    private final ProgressService progressService;

    @Operation(
            summary = "История прогресса дисциплины",
            description = "Возвращает список сохранённых снимков прогресса по выбранной дисциплине.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "История успешно получена"),
                    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа")
            }
    )
    @GetMapping("/{subjectId}/history")
    public List<ProgressSnapshotResponse> getHistory(
            @PathVariable
            @Parameter(description = "ID дисциплины")
            long subjectId) {

        return progressService.getHistory(subjectId);
    }

    @Operation(
            summary = "Данные графика прогресса",
            description = "Возвращает агрегированные данные для построения графика прогресса по дисциплине за выбранный интервал.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные графика успешно получены"),
                    @ApiResponse(responseCode = "400", description = "Некорректный интервал"),
                    @ApiResponse(responseCode = "404", description = "Дисциплина не найдена"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа")
            }
    )
    @GetMapping("/{subjectId}/chart/{interval}")
    public List<ProgressChartDataResponse> getChartData(
            @PathVariable
            @Parameter(description = "ID дисциплины")
            Long subjectId,

            @PathVariable
            @Parameter(
                    description = "Интервал агрегации данных (например: количество дней)"
            )
            Integer interval) {

        return progressService.getChartData(subjectId, interval);
    }
}
