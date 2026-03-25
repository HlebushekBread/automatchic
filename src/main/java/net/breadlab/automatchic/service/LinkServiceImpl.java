package net.breadlab.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.LinkDto;
import net.breadlab.automatchic.model.Link;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.repository.LinkRepository;
import net.breadlab.automatchic.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;
    private final SubjectRepository subjectRepository;

    private long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        try {
            Map<String, String> principal = (Map<String, String>) authentication.getPrincipal();
            return Long.parseLong(principal.get("id"));
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long save(LinkDto linkDto) {
        Link link = (linkDto.getId() != 0)
                ? linkRepository.findById(linkDto.getId()).orElse(null)
                : new Link();

        Subject subject = subjectRepository.findById(linkDto.getSubjectId()).orElse(null);

        if (link == null || subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (linkDto.getId() == 0) {
            if (linkRepository.countBySubjectId(subject.getId()) >= 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит ссылок");
            }
        } else if (subject.getUser().getId() != getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        link.setName(linkDto.getName());
        link.setFullLink(linkDto.getFullLink());
        link.setSubject(subject);

        linkRepository.save(link);

        return link.getId();
    }

    @Override
    public void delete(long id) {
        Link link = linkRepository.findById(id).orElse(null);

        if (link == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (link.getSubject().getUser().getId() != getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        linkRepository.deleteById(id);
    }
}
