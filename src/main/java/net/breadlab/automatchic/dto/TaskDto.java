package net.breadlab.automatchic.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import net.breadlab.automatchic.model.Subject;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    private long id;
    private String name;
    private String type;
    private LocalDateTime dueDate;
    private double maxGrade;
    private double receivedGrade;
    private double gradeWeight;
    private long subjectId;
}
