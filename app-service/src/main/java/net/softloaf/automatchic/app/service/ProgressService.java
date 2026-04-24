package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.common.dto.response.ProgressChartDataResponse;
import net.softloaf.automatchic.common.dto.response.ProgressSnapshotResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProgressService {
    private final RestClient historyClient;
    private final SessionService sessionService;
    private final SubjectRepository subjectRepository;

    public List<ProgressSnapshotResponse> getHistory(long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен");
        }

        return historyClient.get()
                .uri("/{subjectId}/history", subjectId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProgressSnapshotResponse>>() {});
    }

    public List<ProgressChartDataResponse> getChartData(Long subjectId, Integer interval) {

        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен");
        }

        if(interval <= 0 || interval>2000000000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный интервал");
        }

        return historyClient.get()
                .uri("/{subjectId}/chart/{interval}", subjectId, interval)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ProgressChartDataResponse>>() {});
    }
}
