package net.breadlab.automatchic.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.breadlab.automatchic.dto.LinkDto;
import net.breadlab.automatchic.model.Link;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.model.Task;
import net.breadlab.automatchic.repository.LinkRepository;
import net.breadlab.automatchic.repository.SubjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
            return -1;
        } else if (subject.getUser().getId() != getCurrentUserId()) {
            return -2;
        }

        link.setName(linkDto.getName());
        link.setFullLink(linkDto.getFullLink());
        link.setSubject(subject);

        linkRepository.save(link);

        return link.getId();
    }

    @Override
    public long delete(long id) {
        Link link = linkRepository.findById(id).orElse(null);

        if (link == null) {
            return -1;
        } else if (link.getSubject().getUser().getId() != getCurrentUserId()) {
            return -2;
        }

        linkRepository.deleteById(id);
        return 0;
    }
}
