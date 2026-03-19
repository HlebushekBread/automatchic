package net.breadlab.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.TaskDto;
import net.breadlab.automatchic.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PutMapping("/save")
    public ResponseEntity<?> saveTask(@RequestBody TaskDto taskDto) {
        long response = taskService.save(taskDto);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        long response = taskService.delete(id);
        return ResponseEntity.ok().build();
    }
}
