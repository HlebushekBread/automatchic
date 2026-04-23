package net.softloaf.automatchic.history.query.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.common.dto.response.ProgressChartDataResponse;
import net.softloaf.automatchic.common.dto.response.ProgressSnapshotResponse;
import net.softloaf.automatchic.history.query.model.ProgressHistoryEntry;
import net.softloaf.automatchic.history.query.model.ProgressView;
import net.softloaf.automatchic.history.query.repository.ProgressHistoryRepository;
import net.softloaf.automatchic.history.query.repository.ProgressViewRepository;
import net.softloaf.automatchic.history.query.service.ProgressQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/progress")
public class ProgressQueryController {
    private final ProgressViewRepository progressViewRepository;
    private final ProgressHistoryRepository progressHistoryRepository;
    private final ProgressQueryService progressQueryService;

    @GetMapping("/db/{subjectId}")
    public ResponseEntity<ProgressView> getDbSnapshot(@PathVariable Long subjectId) {
        return progressViewRepository.findById(subjectId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/db/{subjectId}/history")
    public List<ProgressHistoryEntry> getDbHistory(@PathVariable Long subjectId) {
        return progressHistoryRepository.findAllBySubjectIdOrderByTimestampAsc(subjectId);
    }

    @GetMapping("/{subjectId}/history")
    public List<ProgressSnapshotResponse> getHistory(@PathVariable Long subjectId) {
        return progressQueryService.buildTimeline(subjectId);
    }

    @GetMapping("/{subjectId}/chart/{interval}")
    public List<ProgressChartDataResponse> getChartData(@PathVariable Long subjectId, @PathVariable Integer interval) {
        return progressQueryService.buildChartPoints(subjectId, Duration.ofMillis(interval));
    }
}
