package net.softloaf.automatchic.app.dto.response;

import lombok.Data;
import net.softloaf.automatchic.app.model.Link;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.model.Task;

import java.util.List;

@Data
public class SubjectBasicResponse {
    private Long id;
    private String name;
    private String teacher;
    private String gradingType;
    private String publicity;
    private UserBasicResponse user;
    private Integer tasksAmount;
    private Integer linksAmount;

    public SubjectBasicResponse(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.teacher = subject.getTeacher();
        this.gradingType = subject.getGradingType().toString();
        this.publicity = subject.getPublicity().toString();
        this.user = new UserBasicResponse(subject.getUser());
        this.tasksAmount = subject.getTasks().size();
        this.linksAmount = subject.getLinks().size();
    }
}
