package net.softloaf.automatchic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class TaskDto {
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
