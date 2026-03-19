package net.breadlab.automatchic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "`task`")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TaskType type;

    @Column(name = "dueDate")
    private LocalDateTime dueDate;

    @Column(name = "max_grade")
    private double maxGrade;

    @Column(name = "received_grade")
    private double receivedGrade;

    @Column(name = "grade_weight")
    private double gradeWeight;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}
