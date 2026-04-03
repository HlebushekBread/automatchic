package net.softloaf.automatchic.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.SubjectRequest;
import net.softloaf.automatchic.model.*;
import net.softloaf.automatchic.repository.SubjectRepository;
import net.softloaf.automatchic.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SessionService sessionService;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Subject> findAllPublic() {
        return subjectRepository.findAllByPublicity(Publicity.PUBLIC);
    }

    @Transactional(readOnly = true)
    public List<Subject> findAllByCurrentUserId() {
        return subjectRepository.findAllByUserId(sessionService.getCurrentUserId());
    }

    @Transactional(readOnly = true)
    public Subject findById(boolean preview, long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if(preview) {
            if(subject.getPublicity() != Publicity.PUBLIC && subject.getUser().getId() != sessionService.getCurrentUserId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на предпросмотр");
            }
        } else {
            if(subject.getUser().getId() != sessionService.getCurrentUserId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещен");
            }
        }

        return subject;
    }

    @Transactional
    public long save(SubjectRequest subjectRequest) {
        if (subjectRequest.getId() == 0) {
            if (subjectRepository.countByUserId(sessionService.getCurrentUserId()) >= 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит предметов");
            }
        }

        Subject subject = (subjectRequest.getId() != 0)
                ? subjectRepository.findById(subjectRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"))
                : new Subject();

        User user = userRepository.findById(sessionService.getCurrentUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID пользователя"));

        if (subjectRequest.getId() != 0 && subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на редактирование");
        }

        subject.setName(subjectRequest.getName());
        subject.setTeacher(subjectRequest.getTeacher());
        subject.setDescription(subjectRequest.getDescription());
        subject.setGradingType(GradingType.valueOf(subjectRequest.getGradingType()));
        subject.setGradingMax(subjectRequest.getGradingMax());
        subject.setGrading5(subjectRequest.getGrading5());
        subject.setGrading4(subjectRequest.getGrading4());
        subject.setGrading3(subjectRequest.getGrading3());
        subject.setGradingMin(subjectRequest.getGradingMin());
        subject.setTargetGrade(subjectRequest.getTargetGrade());
        subject.setPublicity(Publicity.valueOf(subjectRequest.getPublicity()));
        subject.setUser(user);

        subjectRepository.save(subject);

        return subject.getId();
    }

    @Transactional
    public void delete(long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        subjectRepository.deleteById(id);
    }

    @Transactional
    public long copy(long id) {
        if (subjectRepository.countByUserId(sessionService.getCurrentUserId()) >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит предметов");
        }

        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getPublicity() != Publicity.PUBLIC && subject.getUser().getId() != sessionService.getCurrentUserId()) {
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
        subjectCopy.setTargetGrade(3);
        subjectCopy.setPublicity(Publicity.PRIVATE);

        subjectCopy.setUser(userRepository.findById(sessionService.getCurrentUserId()).orElse(null));

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
