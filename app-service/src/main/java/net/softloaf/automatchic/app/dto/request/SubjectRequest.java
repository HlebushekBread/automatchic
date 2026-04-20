package net.softloaf.automatchic.app.dto.request;

import lombok.Data;

@Data
public class SubjectRequest {
    private long id;
    private String name;
    private String teacher;
    private String description;
    private String gradingType;
    private String evaluationType;
    private int targetGrade;
    private double gradingMax;
    private double grading5;
    private double grading4;
    private double grading3;
    private double gradingMin;
    private String publicity;
}
