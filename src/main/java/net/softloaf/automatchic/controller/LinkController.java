package net.softloaf.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.dto.LinkDto;
import net.softloaf.automatchic.service.LinkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/links")
public class LinkController {
    private final LinkService linkService;

    @PutMapping("/save")
    public ResponseEntity<?> saveTask(@RequestBody LinkDto linkDto) {
        long response = linkService.save(linkDto);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        linkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
