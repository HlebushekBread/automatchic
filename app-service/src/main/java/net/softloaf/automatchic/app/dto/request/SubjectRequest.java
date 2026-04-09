package net.softloaf.automatchic.app.dto;

import lombok.Data;

@Data
public class SubjectRequest {
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
