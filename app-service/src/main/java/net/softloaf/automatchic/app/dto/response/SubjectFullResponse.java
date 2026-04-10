package net.softloaf.automatchic.app.dto.response;

import lombok.Data;
import net.softloaf.automatchic.app.model.*;

import java.util.List;

@Data
public class SubjectFullResponse {
    private Long id;
    private String name;
    private String teacher;
    private String description;
    private String gradingType;
    private Double gradingMax;
    private Double grading5;
    private Double grading4;
    private Double grading3;
    private Double gradingMin;
    private Integer targetGrade;
    private String publicity;
    private UserBasicResponse user;
    private List<TaskFullResponse> tasks;
    private List<LinkFullResponse> links;

    public SubjectFullResponse(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.teacher = subject.getTeacher();
        this.description = subject.getDescription();
        this.gradingType = subject.getGradingType().toString();
        this.gradingMax = subject.getGradingMax();
        this.grading5 = subject.getGrading5();
        this.grading4 = subject.getGrading4();
        this.grading3 = subject.getGrading3();
        this.gradingMin = subject.getGradingMin();
        this.targetGrade = subject.getTargetGrade();
        this.publicity = subject.getPublicity().toString();
        this.user = new UserBasicResponse(subject.getUser());
        this.tasks = subject.getTasks().stream().map(TaskFullResponse::new).toList();
        this.links = subject.getLinks().stream().map(LinkFullResponse::new).toList();
    }
}
