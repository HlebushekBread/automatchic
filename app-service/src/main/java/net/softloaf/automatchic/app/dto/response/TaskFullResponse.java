package net.softloaf.automatchic.app.dto.response;

import lombok.Data;
import net.softloaf.automatchic.app.model.Task;
import net.softloaf.automatchic.app.model.TaskType;

import java.time.LocalDateTime;

@Data
public class TaskFullResponse {
    private Long id;
    private String name;
    private TaskType type;
    private LocalDateTime dueDate;
    private Double maxGrade;
    private Double receivedGrade;
    private Double gradeWeight;
    private Integer position;

    public TaskFullResponse(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.type = task.getType();
        this.dueDate = task.getDueDate();
        this.maxGrade = task.getMaxGrade();
        this.receivedGrade = task.getReceivedGrade();
        this.gradeWeight = task.getGradeWeight();
        this.position = task.getPosition();
    }
}
