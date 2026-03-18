package net.breadlab.automatchic.service;

import net.breadlab.automatchic.model.Task;

import java.util.List;

public interface TaskService {
    void findAllBySubjectId(long subjectId);
    void save(Task task);
    void delete(long id);
}
