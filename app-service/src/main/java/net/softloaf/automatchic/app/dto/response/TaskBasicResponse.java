package net.softloaf.automatchic.app.dto.response;

import lombok.Data;
import net.softloaf.automatchic.app.model.Task;
import net.softloaf.automatchic.common.enums.TaskType;

import java.time.LocalDateTime;

@Data
public class TaskBasicResponse {
    private Long id;
    private String name;
    private TaskType type;
    private LocalDateTime dueDate;
    private Long subjectId;

    public TaskBasicResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.type = task.getType();
        this.dueDate = task.getDueDate();
        this.subjectId = task.getSubject().getId();
    }
}
