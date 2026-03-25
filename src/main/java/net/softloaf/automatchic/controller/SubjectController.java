package net.softloaf.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.SubjectDto;
import net.softloaf.automatchic.model.Subject;
import net.softloaf.automatchic.service.SubjectService;
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
    public ResponseEntity<?> saveSubject(@RequestBody SubjectDto subjectDto) {
        long response = subjectService.save(subjectDto);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @PutMapping("/copy/{id}")
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
