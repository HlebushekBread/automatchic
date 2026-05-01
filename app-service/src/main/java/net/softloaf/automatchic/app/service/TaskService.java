package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.TaskRequest;
import net.softloaf.automatchic.app.dto.request.TaskPositionRequest;
import net.softloaf.automatchic.app.dto.response.TaskBasicResponse;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.model.Task;
import net.softloaf.automatchic.app.service.producer.ProgressProducer;
import net.softloaf.automatchic.common.enums.TaskType;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import net.softloaf.automatchic.app.repository.TaskRepository;
import net.softloaf.automatchic.app.service.util.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final SessionService sessionService;
    private final TaskRepository taskRepository;
    private final SubjectRepository subjectRepository;
    private final ProgressProducer progressProducer;

    @Transactional
    public long create(long subjectId, TaskRequest request) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на создание");
        }

        if (taskRepository.countBySubjectId(subjectId) >= 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит задач");
        }

        Task task = new Task();

        task.setName(request.getName());
        task.setType(TaskType.valueOf(request.getType()));
        task.setDueDate(request.getDueDate());
        task.setMaxGrade(request.getMaxGrade());

        task.setReceivedGrade(request.getReceivedGrade());
        task.setGradeWeight(request.getGradeWeight());
        task.setPosition(request.getPosition());
        task.setSubject(subject);

        taskRepository.save(task);

        if (request.getReceivedGrade() > 0) {
            double score = request.getReceivedGrade() * request.getGradeWeight();
            double weight = request.getGradeWeight();

            progressProducer.sendUpdateScoreEvent(subjectId, score, weight);
        }

        return task.getId();
    }

    @Transactional
    public void update(long id, TaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"));

        Subject subject = task.getSubject();

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        task.setName(request.getName());
        task.setType(TaskType.valueOf(request.getType()));
        task.setDueDate(request.getDueDate());
        task.setMaxGrade(request.getMaxGrade());

        if(task.getReceivedGrade() != request.getReceivedGrade() || task.getGradeWeight() != request.getGradeWeight()) {
            double oldScore = task.getReceivedGrade() * task.getGradeWeight();
            double newScore = request.getReceivedGrade() * request.getGradeWeight();
            double scoreDelta = newScore - oldScore;

            double oldWeight = task.getReceivedGrade() > 0 ? task.getGradeWeight() : 0;
            double newWeight = request.getReceivedGrade() > 0 ? request.getGradeWeight() : 0;
            double weightDelta = newWeight - oldWeight;

            if(scoreDelta != 0 || weightDelta != 0) {
                progressProducer.sendUpdateScoreEvent(subject.getId(), scoreDelta, weightDelta);
            }
        }
        task.setReceivedGrade(request.getReceivedGrade());
        task.setGradeWeight(request.getGradeWeight());
        task.setPosition(request.getPosition());

        task.setSubject(subject);

        taskRepository.save(task);
    }

    @Transactional
    public void updatePositions(List<TaskPositionRequest> taskPositionRequests) {
        for(TaskPositionRequest taskPositionRequest : taskPositionRequests) {
            Task task = taskRepository.findById(taskPositionRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"));

            if (task.getSubject().getUser().getId() != sessionService.getCurrentUserId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
            }

            task.setPosition(taskPositionRequest.getPosition());

            taskRepository.save(task);
        }
    }

    @Transactional
    public void delete(long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"));

        if (task.getSubject().getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        double scoreDelta = - task.getReceivedGrade() * task.getGradeWeight();
        double weightDelta = task.getReceivedGrade() > 0
                ? -task.getGradeWeight()
                : 0;
        progressProducer.sendUpdateScoreEvent(task.getSubject().getId(), scoreDelta, weightDelta);

        taskRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TaskBasicResponse> findScheduled(long userId) {
        return taskRepository.findAllBySubjectUserIdAndDueDateIsNotNullOrderByDueDateAsc(userId).stream().map(TaskBasicResponse::new).toList();
    }
}
