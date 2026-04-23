package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.service.ProgressService;
import net.softloaf.automatchic.common.dto.response.ProgressChartDataResponse;
import net.softloaf.automatchic.common.dto.response.ProgressSnapshotResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/progress")
public class ProgressController {
    private final ProgressService progressService;

    @GetMapping("/{subjectId}/history")
    public List<ProgressSnapshotResponse> getHistory(@PathVariable long subjectId) {
        return progressService.getHistory(subjectId);
    }

    @GetMapping("/{subjectId}/chart/{interval}")
    public List<ProgressChartDataResponse> getChartData(@PathVariable Long subjectId, @PathVariable Integer interval) {
        return progressService.getChartData(subjectId, interval);
    }
}
