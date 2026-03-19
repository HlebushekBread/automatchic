package net.breadlab.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.TaskDto;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.model.Task;
import net.breadlab.automatchic.model.TaskType;
import net.breadlab.automatchic.repository.SubjectRepository;
import net.breadlab.automatchic.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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
            return -1;
        } else if (subject.getUser().getId() != getCurrentUserId()) {
            return -2;
        }

        task.setName(taskDto.getName());
        task.setType(TaskType.valueOf(taskDto.getType()));
        task.setDueDate(taskDto.getDueDate());
        task.setMaxGrade(taskDto.getMaxGrade());
        task.setReceivedGrade(taskDto.getReceivedGrade());
        task.setGradeWeight(taskDto.getGradeWeight());
        task.setSubject(subject);

        taskRepository.save(task);

        return task.getId();
    }

    @Transactional
    @Override
    public long delete(long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return -1;
        } else if (task.getSubject().getUser().getId() != getCurrentUserId()) {
            return -2;
        }

        taskRepository.deleteById(id);
        return 0;
    }
}
