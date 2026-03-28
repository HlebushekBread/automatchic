package net.softloaf.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.LinkDto;
import net.softloaf.automatchic.model.Link;
import net.softloaf.automatchic.model.Subject;
import net.softloaf.automatchic.repository.LinkRepository;
import net.softloaf.automatchic.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class LinkService {
    private final SessionService sessionService;
    private final LinkRepository linkRepository;
    private final SubjectRepository subjectRepository;

    public long save(LinkDto linkDto) {
        Link link = (linkDto.getId() != 0)
                ? linkRepository.findById(linkDto.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID ссылки"))
                : new Link();

        Subject subject = subjectRepository.findById(linkDto.getSubjectId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (linkDto.getId() == 0) {
            if (linkRepository.countBySubjectId(subject.getId()) >= 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит ссылок");
            }
        } else if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        link.setName(linkDto.getName());
        link.setFullLink(linkDto.getFullLink());
        link.setSubject(subject);

        linkRepository.save(link);

        return link.getId();
    }

    public void delete(long id) {
        Link link = linkRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID ссылки"));

        if (link.getSubject().getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        linkRepository.deleteById(id);
    }
}
