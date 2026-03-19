package net.breadlab.automatchic.service;

import net.breadlab.automatchic.dto.TaskDto;
import net.breadlab.automatchic.model.Task;

import java.util.List;

public interface TaskService {
    long save(TaskDto taskDto);
    long delete(long id);
}
