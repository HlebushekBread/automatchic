package net.breadlab.automatchic.service;

import io.micrometer.observation.annotation.ObservationKeyValue;
import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.model.Publicity;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.repository.SubjectRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService{
    private final SubjectRepository subjectRepository;

    @Override
    public List<Subject> findAllPublic() {
        return subjectRepository.findAllByPublicity(Publicity.PUBLIC);
    }

    @Override
    public List<Subject> findAllByCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> principal = (HashMap<String, String>) authentication.getPrincipal();
        return subjectRepository.findAllByUserId(Long.parseLong(principal.get("id")));
    }

    @ObservationKeyValue
    public List<Subject> findAllByUserId(long userId) {
        return subjectRepository.findAllByUserId(userId);
    }

    @Override
    public void save(Subject subject) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void copy(long id, long userId) {

    }
}
