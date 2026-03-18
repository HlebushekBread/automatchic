package net.breadlab.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("public")
    public List<Subject> getPublicSubjects() {
        return subjectService.findAllPublic();
    }

    @GetMapping("user")
    public List<Subject> getUserSubjects() {
        return subjectService.findAllByCurrentUserId();
    }
}
