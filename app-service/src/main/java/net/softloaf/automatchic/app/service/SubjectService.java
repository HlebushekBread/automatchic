package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.SubjectRequest;
import net.softloaf.automatchic.app.dto.response.SubjectBasicResponse;
import net.softloaf.automatchic.app.dto.response.SubjectFullResponse;
import net.softloaf.automatchic.app.model.*;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import net.softloaf.automatchic.app.repository.UserRepository;
import net.softloaf.automatchic.app.service.producer.ProgressProducer;
import net.softloaf.automatchic.app.service.util.SearchStringService;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.common.enums.Publicity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubjectService {
    private final ProgressProducer progressProducer;
    private final SessionService sessionService;
    private final SearchStringService searchStringService;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SubjectBasicResponse> findPublic(String query, String gradingType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        String targetQuery = searchStringService.clean(query);
        GradingType targetGradingType;
        try {
            targetGradingType = GradingType.valueOf(gradingType.toUpperCase());
        } catch (IllegalArgumentException e) {
            targetGradingType = null;
        }

        return subjectRepository.findPublicSubjects(targetQuery, targetGradingType , pageable)
                .getContent()
                .stream()
                .map(SubjectBasicResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SubjectFullResponse> findAllByCurrentUserId() {
        return subjectRepository.findAllByUserId(sessionService.getCurrentUserId())
                .stream()
                .map(SubjectFullResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubjectFullResponse findById(boolean preview, long id) {
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

        return new SubjectFullResponse(subject);
    }

    @Transactional
    public long save(SubjectRequest subjectRequest) {
        boolean subjectCreation = false;

        if (subjectRequest.getId() == 0) {
            subjectCreation = true;
            if (subjectRepository.countByUserId(sessionService.getCurrentUserId()) >= 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит дисциплин");
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

        if(!subjectCreation && !subject.getGradingType().toString().equals(subjectRequest.getGradingType())) {
            progressProducer.sendUpdateGradingTypeEvent(subject.getId(), GradingType.valueOf(subjectRequest.getGradingType()));
        }
        subject.setGradingType(GradingType.valueOf(subjectRequest.getGradingType()));

        /*
        if(!subjectCreation && !subject.getEvaluationType().toString().equals(subjectRequest.getEvaluationType())) {
            progressProducer.sendUpdateEvaluationTypeEvent(subject.getId(), EvaluationType.valueOf(subjectRequest.getEvaluationType()));
        }
        subject.setEvaluationType(subjectRequest.getEvaluationType());
         */

        if(!subjectCreation && subject.getTargetGrade() != subjectRequest.getTargetGrade()) {
            progressProducer.sendUpdateTargetGradeEvent(subject.getId(), subjectRequest.getTargetGrade());
        }
        subject.setTargetGrade(subjectRequest.getTargetGrade());

        if(!subjectCreation && (
                subject.getGradingMax() != subjectRequest.getGradingMax() ||
                subject.getGrading5() != subjectRequest.getGrading5() ||
                subject.getGrading4() != subjectRequest.getGrading4() ||
                subject.getGrading3() != subjectRequest.getGrading3() ||
                subject.getGradingMin() != subjectRequest.getGradingMin()
                )
        ) {
            progressProducer.sendUpdateGradingsEvent(subject.getId(),
                    subjectRequest.getGradingMax(),
                    subjectRequest.getGrading5(),
                    subjectRequest.getGrading4(),
                    subjectRequest.getGrading3(),
                    subjectRequest.getGradingMin()
            );
        }
        subject.setGradingMax(subjectRequest.getGradingMax());
        subject.setGrading5(subjectRequest.getGrading5());
        subject.setGrading4(subjectRequest.getGrading4());
        subject.setGrading3(subjectRequest.getGrading3());
        subject.setGradingMin(subjectRequest.getGradingMin());

        subject.setPublicity(Publicity.valueOf(subjectRequest.getPublicity()));

        subject.setSearchString(searchStringService.getSearchString(subject));

        subject.setUser(user);

        subjectRepository.save(subject);

        if(subjectCreation) {
            progressProducer.sendCreateProgressEvent(
                    subject.getId(),
                    0.0, 0.0,
                    subject.getGradingType(),
                    EvaluationType.TOTAL,
                    subject.getTargetGrade(),
                    subject.getGradingMax(),
                    subject.getGrading5(),
                    subject.getGrading4(),
                    subject.getGrading3(),
                    subject.getGradingMin()
            );
        }

        return subject.getId();
    }

    @Transactional
    public void delete(long id) {
        Subject subject = subjectRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Неверный ID дисциплины"));

        if (subject.getUser().getId() != sessionService.getCurrentUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Нет прав на удаление");
        }

        progressProducer.sendDeleteProgressEvent(id);

        subjectRepository.deleteById(id);
    }

    @Transactional
    public long copy(long id) {
        if (subjectRepository.countByUserId(sessionService.getCurrentUserId()) >= 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Достигнут лимит дисциплин");
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
        subjectCopy.setTargetGrade(1);
        subjectCopy.setPublicity(Publicity.PRIVATE);

        subjectCopy.setSearchString(subject.getSearchString());

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

        progressProducer.sendCreateProgressEvent(
                subjectCopy.getId(),
                0.0, 0.0,
                subjectCopy.getGradingType(),
                EvaluationType.TOTAL,
                subjectCopy.getTargetGrade(),
                subjectCopy.getGradingMax(),
                subjectCopy.getGrading5(),
                subjectCopy.getGrading4(),
                subjectCopy.getGrading3(),
                subjectCopy.getGradingMin()
        );

        return subjectCopy.getId();
    }
}
