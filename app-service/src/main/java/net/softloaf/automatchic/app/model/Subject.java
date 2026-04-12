package net.softloaf.automatchic.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "`subject`")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "teacher")
    private String teacher;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "gradingType")
    private GradingType gradingType;

    @Column(name = "grading_max")
    private double gradingMax;

    @Column(name = "grading_5")
    private double grading5;

    @Column(name = "grading_4")
    private double grading4;

    @Column(name = "grading_3")
    private double grading3;

    @Column(name = "grading_min")
    private double gradingMin;

    @Column(name = "target_grade")
    private int targetGrade;

    @Enumerated(EnumType.STRING)
    @Column(name = "publicity")
    private Publicity publicity;

    @Column(name = "search_string")
    private String searchString;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OrderBy("position ASC")
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links;
}
