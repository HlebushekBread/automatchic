package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.common.dto.response.ProgressChartDataResponse;
import net.softloaf.automatchic.common.dto.response.ProgressSnapshotResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProgressService {
    private final RestClient historyClient;

    public List<ProgressSnapshotResponse> getHistory(long subjectId) {
        return historyClient.get()
                .uri("/{subjectId}/history", subjectId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProgressSnapshotResponse>>() {});
    }

    public List<ProgressChartDataResponse> getChartData(Long subjectId, Integer interval) {
        return historyClient.get()
                .uri("/{subjectId}/chart/{interval}", subjectId, interval)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProgressChartDataResponse>>() {});
    }
}
