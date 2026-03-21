package net.breadlab.automatchic.service;

import net.breadlab.automatchic.dto.SubjectDto;
import net.breadlab.automatchic.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllPublic();
    List<Subject> findAllByCurrentUserId();
    Subject findById(boolean preview, long id);
    long save(SubjectDto subjectDto);
    long delete(long id);
    long copy(long id);
}
