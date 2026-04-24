package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.service.ProgressService;
import net.softloaf.automatchic.common.dto.response.ErrorResponse;
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
                    @ApiResponse(
                            responseCode = "200",
                            description = "История успешно получена",
                            content = @Content(schema = @Schema(implementation = ProgressSnapshotResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Нет доступа",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "403",
                                          "message": "Доступ запрещен",
                                          "timestamp": "0"
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
                                          "status": "404",
                                          "message": "Неверный ID дисциплины",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{subjectId}/history")
    public List<ProgressSnapshotResponse> getHistory(
            @PathVariable
            @Parameter(description = "ID дисциплины")
            long subjectId
    ) {
        return progressService.getHistory(subjectId);
    }

    @Operation(
            summary = "Данные графика прогресса",
            description = "Возвращает агрегированные данные для построения графика прогресса по дисциплине за выбранный интервал.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Данные графика успешно получены",
                            content = @Content(schema = @Schema(implementation = ProgressChartDataResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректный интервал",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                        {
                                          "status": "400",
                                          "message": "Некорректный интервал",
                                          "timestamp": "0"
                                        }
                                        """
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
                                          "status": "403",
                                          "message": "Доступ запрещен",
                                          "timestamp": "0"
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
                                          "status": "404",
                                          "message": "Неверный ID дисциплины",
                                          "timestamp": "0"
                                        }
                                        """
                                    )
                            )
                    )
            }
    )
    @GetMapping("/{subjectId}/chart/{interval}")
    public List<ProgressChartDataResponse> getChartData(
            @PathVariable
            @Parameter(description = "ID дисциплины")
            Long subjectId,

            @PathVariable
            @Parameter(description = "Интервал агрегации данных в миллисекундах")
            Integer interval
    ) {
        return progressService.getChartData(subjectId, interval);
    }
}