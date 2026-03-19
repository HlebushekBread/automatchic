package net.breadlab.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.SubjectDto;
import net.breadlab.automatchic.model.*;
import net.breadlab.automatchic.repository.SubjectRepository;
import net.breadlab.automatchic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Subject> findAllByUserId(long userId) {
        return subjectRepository.findAllByUserId(userId);
    }

    @Override
    public Subject findById(long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public long save(SubjectDto subjectDto) {
        Subject subject = (subjectDto.getId() != 0)
                ? subjectRepository.findById(subjectDto.getId()).orElse(null)
                : new Subject();

        User user = userRepository.findById(getCurrentUserId()).orElse(null);

        if (subject == null || user == null) {
            return -1;
        } else if (subjectDto.getId() != 0 && subject.getUser().getId() != getCurrentUserId()) {
            return -2;
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
    public long delete(long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null) {
            return -1;
        } else if (subject.getUser().getId() != getCurrentUserId()) {
            return -2;
        }

        subjectRepository.deleteById(id);
        return 0;
    }

    @Transactional
    @Override
    public long copy(long id) {
        Subject subject = subjectRepository.findById(id).orElse(null);

        if (subject == null ) {
            return -1;
        } else if (subject.getUser().getId() != getCurrentUserId() && subject.getPublicity() == Publicity.PRIVATE) {
            return -2;
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
            taskCopy.setReceivedGrade(task.getReceivedGrade());
            taskCopy.setGradeWeight(task.getGradeWeight());

            taskCopy.setSubject(subjectCopy);

            tasksCopy.add(taskCopy);
        }
        subjectCopy.setTasks(tasksCopy);

        subjectRepository.save(subjectCopy);

        return subjectCopy.getId();
    }
}
