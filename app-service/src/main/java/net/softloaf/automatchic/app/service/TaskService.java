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
    public long save(TaskRequest taskRequest) {

        Task task = (taskRequest.getId() != 0)
                ? taskRepository.findById(taskRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"))
                : new Task();

        Subject subject = subjectRepository.findById(taskRequest.getSubjectId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (taskRequest.getId() == 0) {
            if (taskRepository.countBySubjectId(subject.getId()) >= 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит задач");
            }
        } else if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        task.setName(taskRequest.getName());
        task.setType(TaskType.valueOf(taskRequest.getType()));
        task.setDueDate(taskRequest.getDueDate());
        task.setMaxGrade(taskRequest.getMaxGrade());

        if(task.getReceivedGrade() != taskRequest.getReceivedGrade() || task.getGradeWeight() != taskRequest.getGradeWeight()) {
            double oldScore = task.getReceivedGrade() * task.getGradeWeight();
            double newScore = taskRequest.getReceivedGrade() * taskRequest.getGradeWeight();
            double scoreDelta = newScore - oldScore;

            double oldWeight = task.getReceivedGrade() > 0 ? task.getGradeWeight() : 0;
            double newWeight = taskRequest.getReceivedGrade() > 0 ? taskRequest.getGradeWeight() : 0;
            double weightDelta = newWeight - oldWeight;

            if(scoreDelta != 0 || weightDelta != 0) {
                progressProducer.sendUpdateScoreEvent(subject.getId(), scoreDelta, weightDelta);
            }
        }
        task.setReceivedGrade(taskRequest.getReceivedGrade());
        task.setGradeWeight(taskRequest.getGradeWeight());

        task.setPosition(taskRequest.getPosition());
        task.setSubject(subject);

        taskRepository.save(task);

        return task.getId();
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
