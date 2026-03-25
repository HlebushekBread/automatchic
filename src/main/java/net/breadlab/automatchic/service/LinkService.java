package net.breadlab.automatchic.service;

import net.breadlab.automatchic.dto.LinkDto;

public interface LinkService {
    long save(LinkDto linkDto);
    void delete(long id);
}
