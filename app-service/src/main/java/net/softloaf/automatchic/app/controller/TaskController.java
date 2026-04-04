package net.softloaf.automatchic.app.controller;

import lombok.RequiredArgsConstructor;
import net.softloaf.automatchic.app.dto.TaskRequest;
import net.softloaf.automatchic.app.dto.TaskPositionRequest;
import net.softloaf.automatchic.app.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    @PutMapping("/save")
    public ResponseEntity<?> saveTask(@RequestBody TaskRequest taskRequest) {
        long response = taskService.save(taskRequest);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @PatchMapping("/positions")
    public ResponseEntity<?> updatePositions(@RequestBody List<TaskPositionRequest> positions) {
        taskService.updatePositions(positions);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
