package net.softloaf.automatchic.dto;

import lombok.Data;

@Data
public class LinkRequest {
    private long id;
    private String name;
    private String fullLink;
    private long subjectId;
}
