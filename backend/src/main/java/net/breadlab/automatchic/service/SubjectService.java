package net.breadlab.automatchic.service;

import net.breadlab.automatchic.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllPublic();
    List<Subject> findAllByCurrentUserId();
    List<Subject> findAllByUserId(long userId);
    void save(Subject subject);
    void delete(long id);
    void copy(long id, long userId);
}
