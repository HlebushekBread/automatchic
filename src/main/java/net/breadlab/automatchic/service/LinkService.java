package net.breadlab.automatchic.service;

import net.breadlab.automatchic.dto.LinkDto;

public interface LinkService {
    long save(LinkDto linkDto);
    long delete(long id);
}
