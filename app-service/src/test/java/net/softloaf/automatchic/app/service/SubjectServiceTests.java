package net.softloaf.automatchic.app.service;

import net.softloaf.automatchic.app.dto.request.SubjectRequest;
import net.softloaf.automatchic.app.dto.response.SubjectBasicResponse;
import net.softloaf.automatchic.app.dto.response.SubjectFullResponse;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.model.User;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import net.softloaf.automatchic.app.repository.UserRepository;
import net.softloaf.automatchic.app.service.producer.ProgressProducer;
import net.softloaf.automatchic.app.service.util.SearchStringService;
import net.softloaf.automatchic.app.service.util.SessionService;
import net.softloaf.automatchic.common.enums.EvaluationType;
import net.softloaf.automatchic.common.enums.GradingType;
import net.softloaf.automatchic.common.enums.Publicity;
import net.softloaf.automatchic.common.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTests {
    @Mock
    private ProgressProducer progressProducer;
    @Mock
    private SessionService sessionService;
    @Mock
    private SearchStringService searchStringService;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void findPublic_shouldReturnSubjects() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");
        subject.setTeacher("Ivanov");
        subject.setDescription("desc");
        subject.setGradingType(GradingType.EXAM);
        subject.setEvaluationType(EvaluationType.AVERAGE);
        subject.setTargetGrade(3);
        subject.setGradingMax(100);
        subject.setGrading5(90);
        subject.setGrading4(75);
        subject.setGrading3(60);
        subject.setGradingMin(0);
        subject.setPublicity(Publicity.PRIVATE);
        subject.setTasks(List.of());
        subject.setLinks(List.of());

        User user = new User();
        user.setId(1L);
        user.setRole(Role.STUDENT);
        subject.setUser(user);

        Page<Subject> page = new PageImpl<>(List.of(subject));

        when(searchStringService.clean("math"))
                .thenReturn("math");

        when(subjectRepository.findPublicSubjects(
                eq("math"),
                eq(GradingType.EXAM),
                any(Pageable.class)))
                .thenReturn(page);

        List<SubjectBasicResponse> result =
                subjectService.findPublic("math", "EXAM", 0, 10);

        assertEquals(1, result.size());
        verify(subjectRepository).findPublicSubjects(
                eq("math"),
                eq(GradingType.EXAM),
                any(Pageable.class));
    }

    @Test
    void findAllByCurrentUserId_shouldReturnList() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Math");
        subject.setTeacher("Ivanov");
        subject.setDescription("desc");
        subject.setGradingType(GradingType.EXAM);
        subject.setEvaluationType(EvaluationType.AVERAGE);
        subject.setTargetGrade(3);
        subject.setGradingMax(100);
        subject.setGrading5(90);
        subject.setGrading4(75);
        subject.setGrading3(60);
        subject.setGradingMin(0);
        subject.setPublicity(Publicity.PRIVATE);
        subject.setTasks(List.of());
        subject.setLinks(List.of());

        User user = new User();
        user.setId(5L);
        user.setRole(Role.STUDENT);
        subject.setUser(user);

        when(sessionService.getCurrentUserId()).thenReturn(5L);
        when(subjectRepository.findAllByUserId(5L))
                .thenReturn(List.of(subject));

        List<SubjectFullResponse> result =
                subjectService.findAllByCurrentUserId();

        assertEquals(1, result.size());
    }

    @Test
    void findById_shouldReturnSubject_whenOwner() {
        Subject subject = new Subject();
        subject.setId(10L);
        subject.setName("Math");
        subject.setTeacher("Ivanov");
        subject.setDescription("desc");
        subject.setGradingType(GradingType.EXAM);
        subject.setEvaluationType(EvaluationType.AVERAGE);
        subject.setTargetGrade(3);
        subject.setGradingMax(100);
        subject.setGrading5(90);
        subject.setGrading4(75);
        subject.setGrading3(60);
        subject.setGradingMin(0);
        subject.setPublicity(Publicity.PRIVATE);
        subject.setTasks(List.of());
        subject.setLinks(List.of());

        User user = new User();
        user.setId(1L);
        user.setRole(Role.STUDENT);
        subject.setUser(user);

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.of(subject));

        when(sessionService.getCurrentUserId())
                .thenReturn(1L);

        SubjectFullResponse response =
                subjectService.findById(false, 10L);

        assertNotNull(response);
    }

    @Test
    void findById_shouldThrowForbidden_whenNotOwner() {
        User user = new User();
        user.setId(2L);

        Subject subject = new Subject();
        subject.setUser(user);

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.of(subject));

        when(sessionService.getCurrentUserId())
                .thenReturn(1L);

        assertThrows(ResponseStatusException.class,
                () -> subjectService.findById(false, 10L));
    }

    @Test
    void save_shouldCreateSubject() {
        SubjectRequest request = new SubjectRequest();
        request.setId(0);
        request.setName("Math");
        request.setTeacher("Ivanov");
        request.setDescription("desc");
        request.setGradingType("EXAM");
        request.setEvaluationType("AVERAGE");
        request.setTargetGrade(3);
        request.setGradingMax(100);
        request.setGrading5(90);
        request.setGrading4(75);
        request.setGrading3(60);
        request.setGradingMin(0);
        request.setPublicity("PRIVATE");

        User user = new User();
        user.setId(1L);

        when(sessionService.getCurrentUserId()).thenReturn(1L);
        when(subjectRepository.countByUserId(1L)).thenReturn(0L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(searchStringService.getSearchString(any(Subject.class)))
                .thenReturn("math");

        when(subjectRepository.save(any(Subject.class)))
                .thenAnswer(inv -> {
                    Subject s = inv.getArgument(0);
                    s.setId(99L);
                    return s;
                });

        long id = subjectService.save(request);

        assertEquals(99L, id);

        verify(subjectRepository).save(any(Subject.class));
        verify(progressProducer)
                .sendCreateProgressEvent(
                        eq(99L),
                        eq(0.0),
                        eq(0.0),
                        any(),
                        any(),
                        eq(3),
                        eq(100D),
                        eq(90D),
                        eq(75D),
                        eq(60D),
                        eq(0D)
                );
    }

    @Test
    void delete_shouldDeleteSubject() {
        User user = new User();
        user.setId(1L);

        Subject subject = new Subject();
        subject.setId(15L);
        subject.setUser(user);

        when(subjectRepository.findById(15L))
                .thenReturn(Optional.of(subject));

        when(sessionService.getCurrentUserId())
                .thenReturn(1L);

        subjectService.delete(15L);

        verify(progressProducer)
                .sendDeleteProgressEvent(15L);

        verify(subjectRepository)
                .deleteById(15L);
    }

    @Test
    void copy_shouldCreateCopy() {
        User owner = new User();
        owner.setId(2L);

        User current = new User();
        current.setId(1L);

        Subject subject = new Subject();
        subject.setId(10L);
        subject.setUser(owner);
        subject.setPublicity(Publicity.PUBLIC);
        subject.setName("Math");
        subject.setGradingType(GradingType.EXAM);
        subject.setEvaluationType(EvaluationType.AVERAGE);

        subject.setTasks(List.of());
        subject.setLinks(List.of());

        when(sessionService.getCurrentUserId()).thenReturn(1L);
        when(subjectRepository.countByUserId(1L)).thenReturn(0L);
        when(subjectRepository.findById(10L))
                .thenReturn(Optional.of(subject));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(current));

        when(subjectRepository.save(any(Subject.class)))
                .thenAnswer(inv -> {
                    Subject s = inv.getArgument(0);
                    s.setId(55L);
                    return s;
                });

        long id = subjectService.copy(10L);

        assertEquals(55L, id);

        verify(subjectRepository).save(any(Subject.class));
        verify(progressProducer)
                .sendCreateProgressEvent(
                        eq(55L),
                        eq(0.0),
                        eq(0.0),
                        any(),
                        any(),
                        anyInt(),
                        anyDouble(),
                        anyDouble(),
                        anyDouble(),
                        anyDouble(),
                        anyDouble()
                );
    }
}