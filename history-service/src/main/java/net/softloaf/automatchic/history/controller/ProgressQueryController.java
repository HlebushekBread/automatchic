package net.softloaf.automatchic.history.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.history.query.ProgressHistoryEntry;
import net.softloaf.automatchic.history.query.ProgressHistoryRepository;
import net.softloaf.automatchic.history.query.ProgressView;
import net.softloaf.automatchic.history.query.ProgressViewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/progress")
public class ProgressQueryController {
    private final ProgressViewRepository progressViewRepository;
    private final ProgressHistoryRepository progressHistoryRepository;

    @GetMapping("/{subjectId}")
    public ResponseEntity<ProgressView> getBalance(@PathVariable long subjectId) {
        return progressViewRepository.findById(subjectId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{subjectId}/history")
    public List<ProgressHistoryEntry> getHistory(@PathVariable long subjectId) {
        return progressHistoryRepository.findAllBySubjectIdOrderByTimestampAsc(subjectId);
    }
}
