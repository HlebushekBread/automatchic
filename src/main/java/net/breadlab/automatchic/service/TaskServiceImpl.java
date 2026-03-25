package net.breadlab.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.TaskDto;
import net.breadlab.automatchic.dto.TaskPositionDto;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.model.Task;
import net.breadlab.automatchic.model.TaskType;
import net.breadlab.automatchic.repository.SubjectRepository;
import net.breadlab.automatchic.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final SubjectRepository subjectRepository;

    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        try {
            Map<String, String> principal = (Map<String, String>) authentication.getPrincipal();
            return Long.parseLong(principal.get("id"));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long save(TaskDto taskDto) {

        Task task = (taskDto.getId() != 0)
                ? taskRepository.findById(taskDto.getId()).orElse(null)
                : new Task();

        Subject subject = subjectRepository.findById(taskDto.getSubjectId()).orElse(null);

        if (task == null || subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (taskDto.getId() == 0) {
            if (taskRepository.countBySubjectId(subject.getId()) >= 20) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит задач");
            }
        } else if (subject.getUser().getId() != getCurrentUserId()) {
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
    @Override
    public void updatePositions(List<TaskPositionDto> taskPositionDtos) {
        for(TaskPositionDto taskPositionDto : taskPositionDtos) {
            Task task = taskRepository.findById(taskPositionDto.getId()).orElse(null);

            if (task == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
            } else if (task.getSubject().getUser().getId() != getCurrentUserId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
            }

            task.setPosition(taskPositionDto.getPosition());

            taskRepository.save(task);
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (task.getSubject().getUser().getId() != getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        taskRepository.deleteById(id);
    }
}
