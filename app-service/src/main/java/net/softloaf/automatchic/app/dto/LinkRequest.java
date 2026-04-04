package net.softloaf.automatchic.app.dto;

import lombok.Data;

@Data
public class LinkRequest {
    private long id;
    private String name;
    private String fullLink;
    private long subjectId;
}
