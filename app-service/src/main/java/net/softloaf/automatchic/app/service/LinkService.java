package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.LinkRequest;
import net.softloaf.automatchic.app.model.Link;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.repository.LinkRepository;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class LinkService {
    private final SessionService sessionService;
    private final LinkRepository linkRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public long save(LinkRequest linkRequest) {
        Link link = (linkRequest.getId() != 0)
                ? linkRepository.findById(linkRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID ссылки"))
                : new Link();

        Subject subject = subjectRepository.findById(linkRequest.getSubjectId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (linkRequest.getId() == 0) {
            if (linkRepository.countBySubjectId(subject.getId()) >= 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит ссылок");
            }
        } else if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        link.setName(linkRequest.getName());
        link.setFullLink(linkRequest.getFullLink());
        link.setSubject(subject);

        linkRepository.save(link);

        return link.getId();
    }

    @Transactional
    public void delete(long id) {
        Link link = linkRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID ссылки"));

        if (link.getSubject().getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        linkRepository.deleteById(id);
    }
}
