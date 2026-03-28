package net.softloaf.automatchic.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LinkDto {
    private long id;
    private String name;
    private String fullLink;
    private long subjectId;
}
