package net.softloaf.automatchic.service;

import net.softloaf.automatchic.dto.SubjectDto;
import net.softloaf.automatchic.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllPublic();
    List<Subject> findAllByCurrentUserId();
    Subject findById(boolean preview, long id);
    long save(SubjectDto subjectDto);
    void delete(long id);
    long copy(long id);
}
