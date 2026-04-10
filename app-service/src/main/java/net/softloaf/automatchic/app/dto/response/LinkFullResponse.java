package net.softloaf.automatchic.app.dto.response;

import lombok.Data;
import net.softloaf.automatchic.app.model.Link;

@Data
public class LinkFullResponse {
    private long id;
    private String name;
    private String fullLink;

    public LinkFullResponse(Link link) {
        this.id = link.getId();
        this.name = link.getName();
        this.fullLink = link.getFullLink();
    }
}
