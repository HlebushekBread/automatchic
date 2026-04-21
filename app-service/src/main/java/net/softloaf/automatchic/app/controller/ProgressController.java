package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
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
    private final RestClient historyClient;

    @GetMapping("/{subjectId}/history")
    public List<ProgressSnapshotResponse> getHistory(@PathVariable long subjectId) {
        return historyClient.get()
                .uri("/{subjectId}/history", subjectId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProgressSnapshotResponse>>() {});
    }

    @GetMapping("/{subjectId}/chart")
    public List<ProgressChartDataResponse> getChartData(@PathVariable Long subjectId) {
        return historyClient.get()
                .uri("/{subjectId}/chart", subjectId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProgressChartDataResponse>>() {});
    }
}
