package net.softloaf.automatchic.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.LinkRequest;
import net.softloaf.automatchic.app.service.LinkService;
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
                    @ApiResponse(responseCode = "200", description = "Ссылка успешно сохранена"),
                    @ApiResponse(responseCode = "400", description = "Достигнут лимит ссылок"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Ссылка или дисциплина не найдены")
            }
    )
    @PutMapping("/save")
    public ResponseEntity<?> saveTask(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные ссылки",
                    required = true
            )
            @RequestBody LinkRequest linkRequest) {

        long response = linkService.save(linkRequest);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @Operation(
            summary = "Удалить ссылку",
            description = "Удаляет ссылку по ID. Доступно только владельцу дисциплины.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Ссылка удалена"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа"),
                    @ApiResponse(responseCode = "404", description = "Ссылка не найдена")
            }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(
            @PathVariable
            @Parameter(description = "ID ссылки")
            long id) {

        linkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
