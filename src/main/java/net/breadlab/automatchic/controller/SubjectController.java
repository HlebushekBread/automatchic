package net.breadlab.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.SubjectDto;
import net.breadlab.automatchic.model.Subject;
import net.breadlab.automatchic.service.SubjectService;
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
        long response = subjectService.delete(id);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }
}
