package net.softloaf.automatchic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class SubjectDto {
    private long id;
    private String name;
    private String teacher;
    private String description;
    private String gradingType;
    private double gradingMax;
    private double grading5;
    private double grading4;
    private double grading3;
    private double gradingMin;
    private int targetGrade;
    private String publicity;
}
