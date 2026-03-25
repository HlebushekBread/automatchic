package net.softloaf.automatchic.service;

import net.softloaf.automatchic.dto.LinkDto;

public interface LinkService {
    long save(LinkDto linkDto);
    void delete(long id);
}
