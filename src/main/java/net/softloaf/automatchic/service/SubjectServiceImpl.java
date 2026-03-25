package net.softloaf.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.SubjectDto;
import net.breadlab.automatchic.model.*;
import net.softloaf.automatchic.model.*;
import net.softloaf.automatchic.repository.SubjectRepository;
import net.softloaf.automatchic.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

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
    public List<Subject> findAllPublic() {
        return subjectRepository.findAllByPublicity(Publicity.PUBLIC);
    }

    @Override
    public List<Subject> findAllByCurrentUserId() {
        return subjectRepository.findAllByUserId(getCurrentUserId());
    }

    @Override
    public Subject findById(boolean preview, long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);

        if(subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else {
            if(preview) {
                if(subject.getPublicity() != Publicity.PUBLIC && subject.getUser().getId() != getCurrentUserId()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на предпросмотр");
                }
            } else {
                if(subject.getUser().getId() != getCurrentUserId()) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен");
                }
            }
        }
        return subject;
    }

    @Transactional
    @Override
    public long save(SubjectDto subjectDto) {
        if (subjectDto.getId() == 0) {
            if (subjectRepository.countByUserId(getCurrentUserId()) >= 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит предметов");
            }
        }

        Subject subject = (subjectDto.getId() != 0)
                ? subjectRepository.findById(subjectDto.getId()).orElse(null)
                : new Subject();

        User user = userRepository.findById(getCurrentUserId()).orElse(null);

        if (subject == null || user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (subjectDto.getId() != 0 && subject.getUser().getId() != getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        subject.setName(subjectDto.getName());
        subject.setTeacher(subjectDto.getTeacher());
        subject.setDescription(subjectDto.getDescription());
        subject.setGradingType(GradingType.valueOf(subjectDto.getGradingType()));
        subject.setGradingMax(subjectDto.getGradingMax());
        subject.setGrading5(subjectDto.getGrading5());
        subject.setGrading4(subjectDto.getGrading4());
        subject.setGrading3(subjectDto.getGrading3());
        subject.setGradingMin(subjectDto.getGradingMin());
        subject.setTargetGrade(subjectDto.getTargetGrade());
        subject.setPublicity(Publicity.valueOf(subjectDto.getPublicity()));
        subject.setUser(user);

        subjectRepository.save(subject);

        return subject.getId();
    }

    @Transactional
    @Override
    public void delete(long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (subject.getUser().getId() != getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        subjectRepository.deleteById(id);
    }

    @Transactional
    @Override
    public long copy(long id) {
        if (subjectRepository.countByUserId(getCurrentUserId()) >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит предметов");
        }

        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID");
        } else if (subject.getPublicity() != Publicity.PUBLIC && subject.getUser().getId() != getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на копирование");
        }

        Subject subjectCopy = new Subject();

        subjectCopy.setName(subject.getName());
        subjectCopy.setTeacher(subject.getTeacher());
        subjectCopy.setDescription(subject.getDescription());
        subjectCopy.setGradingType(subject.getGradingType());
        subjectCopy.setGradingMax(subject.getGradingMax());
        subjectCopy.setGrading5(subject.getGrading5());
        subjectCopy.setGrading4(subject.getGrading4());
        subjectCopy.setGrading3(subject.getGrading3());
        subjectCopy.setGradingMin(subject.getGradingMin());
        subjectCopy.setTargetGrade(subject.getTargetGrade());
        subjectCopy.setPublicity(subject.getPublicity());

        subjectCopy.setUser(userRepository.findById(getCurrentUserId()).orElse(null));

        List<Task> tasksCopy = new ArrayList<>();
        for (Task task : subject.getTasks()) {
            Task taskCopy = new Task();

            taskCopy.setName(task.getName());
            taskCopy.setType(task.getType());
            taskCopy.setDueDate(task.getDueDate());
            taskCopy.setMaxGrade(task.getMaxGrade());
            taskCopy.setReceivedGrade(0);
            taskCopy.setGradeWeight(task.getGradeWeight());
            taskCopy.setPosition(task.getPosition());

            taskCopy.setSubject(subjectCopy);

            tasksCopy.add(taskCopy);
        }
        subjectCopy.setTasks(tasksCopy);

        List<Link> linksCopy = new ArrayList<>();
        for (Link link : subject.getLinks()) {
            Link linkCopy = new Link();

            linkCopy.setName(link.getName());
            linkCopy.setFullLink(link.getFullLink());

            linkCopy.setSubject(subjectCopy);

            linksCopy.add(linkCopy);
        }
        subjectCopy.setLinks(linksCopy);

        subjectRepository.save(subjectCopy);

        return subjectCopy.getId();
    }
}
