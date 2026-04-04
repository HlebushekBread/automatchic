package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.SubjectRequest;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/view/{id}")
    public Subject getSubjectViewById(@PathVariable long id) {
        return subjectService.findById(false, id);
    }

    @GetMapping("/preview/{id}")
    public Subject getSubjectPreviewById(@PathVariable long id) {
        return subjectService.findById(true, id);
    }

    @GetMapping("/public")
    public List<Subject> getPublicSubjects() {
        return subjectService.findAllPublic();
    }

    @GetMapping("/user")
    public List<Subject> getUserSubjects() {
        return subjectService.findAllByCurrentUserId();
    }

    @PutMapping("/save")
    public ResponseEntity<?> saveSubject(@RequestBody SubjectRequest subjectRequest) {
        long response = subjectService.save(subjectRequest);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @GetMapping("/copy/{id}")
    public ResponseEntity<?> copySubject(@PathVariable long id) {
        long response = subjectService.copy(id);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
