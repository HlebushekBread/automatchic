package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.request.SubjectRequest;
import net.softloaf.automatchic.app.dto.response.SubjectBasicResponse;
import net.softloaf.automatchic.app.dto.response.SubjectFullResponse;
import net.softloaf.automatchic.app.model.Subject;
import net.softloaf.automatchic.app.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping("/view/{id}")
    public SubjectFullResponse getSubjectViewById(@PathVariable long id) {
        return subjectService.findById(false, id);
    }

    @GetMapping("/preview/{id}")
    public SubjectFullResponse getSubjectPreviewById(@PathVariable long id) {
        return subjectService.findById(true, id);
    }

    @GetMapping("/public")
    public List<SubjectBasicResponse> getPublicSubjects(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return subjectService.findPublic(query, type, page, size);
    }

    @GetMapping("/user")
    public List<SubjectFullResponse> getUserSubjects() {
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
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
