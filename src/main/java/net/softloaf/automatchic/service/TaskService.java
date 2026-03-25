package net.softloaf.automatchic.service;

import net.softloaf.automatchic.dto.TaskDto;
import net.softloaf.automatchic.dto.TaskPositionDto;

import java.util.List;

public interface TaskService {
    long save(TaskDto taskDto);
    void updatePositions(List<TaskPositionDto> taskPositionDtos);
    void delete(long id);
}
