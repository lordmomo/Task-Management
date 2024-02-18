package com.momo.taskManagement.service.interfaces;

import com.momo.taskManagement.dto.FileDownloadDto;
import com.momo.taskManagement.dto.TaskRequestDto;
import com.momo.taskManagement.userFolder.model.Task;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    String saveTask(TaskRequestDto requestDto);

    String assignTask(String username, Task task);

    ResponseEntity<List<Task>> showDetails(Long userTaskId);

    String updateTask(String taskName, Task task);

    String removeTask(String taskName);

    List<TaskRequestDto> viewAllTasks();

    FileDownloadDto downloadFile(Long id);
}
