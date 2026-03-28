package net.softloaf.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.TaskDto;
import net.softloaf.automatchic.dto.TaskPositionDto;
import net.softloaf.automatchic.model.Subject;
import net.softloaf.automatchic.model.Task;
import net.softloaf.automatchic.model.TaskType;
import net.softloaf.automatchic.repository.SubjectRepository;
import net.softloaf.automatchic.repository.TaskRepository;
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

    public long save(TaskDto taskDto) {

        Task task = (taskDto.getId() != 0)
                ? taskRepository.findById(taskDto.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"))
                : new Task();

        Subject subject = subjectRepository.findById(taskDto.getSubjectId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (taskDto.getId() == 0) {
            if (taskRepository.countBySubjectId(subject.getId()) >= 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит задач");
            }
        } else if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        task.setName(taskDto.getName());
        task.setType(TaskType.valueOf(taskDto.getType()));
        task.setDueDate(taskDto.getDueDate());
        task.setMaxGrade(taskDto.getMaxGrade());
        task.setReceivedGrade(taskDto.getReceivedGrade());
        task.setGradeWeight(taskDto.getGradeWeight());
        task.setPosition(taskDto.getPosition());
        task.setSubject(subject);

        taskRepository.save(task);

        return task.getId();
    }

    @Transactional
    public void updatePositions(List<TaskPositionDto> taskPositionDtos) {
        for(TaskPositionDto taskPositionDto : taskPositionDtos) {
            Task task = taskRepository.findById(taskPositionDto.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"));

            if (task.getSubject().getUser().getId() != sessionService.getCurrentUserId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
            }

            task.setPosition(taskPositionDto.getPosition());

            taskRepository.save(task);
        }
    }

    @Transactional
    public void delete(long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID задачи"));

        if (task.getSubject().getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        taskRepository.deleteById(id);
    }
}
