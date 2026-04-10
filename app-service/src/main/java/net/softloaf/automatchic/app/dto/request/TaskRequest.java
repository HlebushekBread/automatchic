package net.softloaf.automatchic.app.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {
    private long id;
    private String name;
    private String type;
    private LocalDateTime dueDate;
    private double maxGrade;
    private double receivedGrade;
    private double gradeWeight;
    private int position;
    private long subjectId;
}
