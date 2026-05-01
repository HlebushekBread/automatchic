package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.LinkRequest;
import net.softloaf.automatchic.app.model.Link;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.repository.LinkRepository;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import net.softloaf.automatchic.app.service.util.SessionService;
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
    public long create(long subjectId, LinkRequest request) {

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        if (linkRepository.countBySubjectId(subjectId) >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит ссылок");
        }

        Link link = new Link();
        link.setName(request.getName());
        link.setFullLink(request.getFullLink());
        link.setSubject(subject);

        linkRepository.save(link);

        return link.getId();
    }

    @Transactional
    public void update(long id, LinkRequest request) {

        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID ссылки"));

        Subject subject = link.getSubject();

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        link.setName(request.getName());
        link.setFullLink(request.getFullLink());

        linkRepository.save(link);
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
