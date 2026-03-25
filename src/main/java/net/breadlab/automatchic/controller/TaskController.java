package net.breadlab.automatchic.controller;

import lombok.RequiredArgsConstructor;
import net.breadlab.automatchic.dto.TaskDto;
import net.breadlab.automatchic.dto.TaskPositionDto;
import net.breadlab.automatchic.service.TaskService;
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
    public ResponseEntity<?> saveTask(@RequestBody TaskDto taskDto) {
        long response = taskService.save(taskDto);
        return new ResponseEntity<>(Map.of("id", response), HttpStatus.OK);
    }

    @PatchMapping("/positions")
    public ResponseEntity<?> updatePositions(@RequestBody List<TaskPositionDto> positions) {
        taskService.updatePositions(positions);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
