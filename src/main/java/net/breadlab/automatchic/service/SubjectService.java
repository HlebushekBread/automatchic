package net.breadlab.automatchic.service;

import net.breadlab.automatchic.dto.SubjectDto;
import net.breadlab.automatchic.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllPublic();
    List<Subject> findAllByCurrentUserId();
    List<Subject> findAllByUserId(long userId);
    Subject findById(long id);
    long save(SubjectDto subjectDto);
    long delete(long id);
    long copy(long id);
}
