package net.breadlab.automatchic.service;

import net.breadlab.automatchic.dto.TaskDto;
import net.breadlab.automatchic.dto.TaskPositionDto;
import net.breadlab.automatchic.model.Task;

import java.util.List;

public interface TaskService {
    long save(TaskDto taskDto);
    void updatePositions(List<TaskPositionDto> taskPositionDtos);
    void delete(long id);
}
