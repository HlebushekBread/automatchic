package net.breadlab.automatchic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkDto {
    private long id;
    private String name;
    private String fullLink;
    private long subjectId;
}
