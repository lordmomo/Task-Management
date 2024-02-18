package com.momo.taskManagement.controller;

import com.momo.taskManagement.dto.TaskRequestDto;
import com.momo.taskManagement.userFolder.model.Task;
import com.momo.taskManagement.service.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskServiceImpl taskService;


    @PostMapping("/add-task")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addTask(@RequestParam("taskName") String taskName,
                                          @RequestParam("taskDescription") String taskDescription,
                                          @RequestParam("taskStartDate") Date taskStartDate,
                                          @RequestParam("taskEndDate") Date taskEndDate,
                                          @RequestParam("taskPriority") String taskPriority,
                                          @RequestParam("taskCategory") String taskCategory,
                                          @RequestParam(value = "file", required = false) MultipartFile file){

        TaskRequestDto requestDto = new TaskRequestDto(taskName,taskDescription,taskStartDate,taskEndDate,taskPriority,taskCategory,file);

        String response = taskService.saveTask(requestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/assign-task/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> assignTask(@PathVariable("username") String username, @RequestBody Task task){
            String message =taskService.assignTask(username,task);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(message);
    }

    @PutMapping("/update-task/{taskName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateTask(@PathVariable("taskName") String taskName,@RequestBody Task task){
        String message = taskService.updateTask(taskName,task);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }

    @DeleteMapping("/remove-task/{taskName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> removeTask(@PathVariable("taskName") String taskName){
        String message = taskService.removeTask(taskName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }

    @GetMapping("/view-all-tasks")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<TaskRequestDto> viewAllTasks(){
        return taskService.viewAllTasks();
    }

}
