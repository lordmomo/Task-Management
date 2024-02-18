package com.momo.taskManagement.service.impl;

import com.momo.taskManagement.dto.FileDownloadDto;
import com.momo.taskManagement.dto.TaskRequestDto;
import com.momo.taskManagement.exception.TaskNotFoundException;
import com.momo.taskManagement.userFolder.model.Task;
import com.momo.taskManagement.userFolder.model.User;
import com.momo.taskManagement.userFolder.repository.TaskRepository;
import com.momo.taskManagement.userFolder.repository.UserRepository;
import com.momo.taskManagement.service.interfaces.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    public String saveTask(TaskRequestDto requestDto) {
        try {
            Task.TaskBuilder taskBuilder= Task.builder()
                    .taskName(requestDto.taskName())
                    .taskDescription(requestDto.taskDescription())
                    .taskStartDate(requestDto.taskStartDate())
                    .taskEndDate(requestDto.taskEndDate())
                    .taskCategory(requestDto.taskCategory())
                    .taskPriority(requestDto.taskPriority());

            if(requestDto.file() != null && !requestDto.file().isEmpty()){
                taskBuilder.data(requestDto.file().getBytes())
                        .fileName(requestDto.file().getOriginalFilename())
                        .fileType(requestDto.file().getContentType());
            }
            Task task = taskBuilder.build();
            taskRepository.save(task);
            log.info("Task Added Successfully!!!");
            return "Task Added.";
        } catch (Exception e) {
            return "Failed adding Task";
        }
    }

    public String assignTask(String username, Task task) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            log.info("User found: "+username);
            User user = optionalUser.get();

            if (!Objects.nonNull(user.getTaskId())) {
                user.setTaskId(task);
            }
            userRepository.save(user);
            log.info("Task assigned Successfully!!!");
            return "Task Assigned Successfully";
        }
        return "User not found";

    }

    public ResponseEntity<List<Task>> showDetails(Long userTaskId) {
        List<Task> task = (List<Task>)taskRepository.findAllById(Collections.singleton(userTaskId));
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    public String updateTask(String taskName, Task task) {

        Task updtTask = checkTask(taskName);

            if (Objects.nonNull(task.getTaskName()) && !"".equalsIgnoreCase(task.getTaskName())) {
                updtTask.setTaskName(task.getTaskName());
            }
            if (Objects.nonNull(task.getTaskDescription()) && !"".equalsIgnoreCase(task.getTaskDescription())) {
                updtTask.setTaskDescription(task.getTaskDescription());
            }
            if (Objects.nonNull(task.getTaskStartDate())) {
                updtTask.setTaskStartDate(task.getTaskStartDate());
            }
            if (Objects.nonNull(task.getTaskCategory()) && !"".equalsIgnoreCase(task.getTaskCategory())) {
                updtTask.setTaskCategory(task.getTaskCategory());
            }
            if (Objects.nonNull(task.getTaskPriority()) && !"".equalsIgnoreCase(task.getTaskPriority())) {
                updtTask.setTaskPriority(task.getTaskPriority());
            }
            taskRepository.save(updtTask);
            return "Successfully Task Updated ";

    }

    @Override
    public String removeTask(String taskName) {
        Task task = checkTask(taskName);
        taskRepository.delete(task);
        return "Task deleted Successfully";
    }

    public List<TaskRequestDto> viewAllTasks() {
        List<Task> optTask = (List<Task>) taskRepository.findAll();
        List<TaskRequestDto> taskDtoList = new ArrayList<>();

        for(Task task : optTask){
            taskDtoList.add(modelMapper.map(optTask,TaskRequestDto.class));
        }
        return taskDtoList;
    }

    public FileDownloadDto downloadFile(Long id){
        try{
            Optional<Task> optionalTaskFile = taskRepository.findById(id);

            if(optionalTaskFile.isPresent()){
                Task downloadFile = optionalTaskFile.get();

                byte[] content = downloadFile.getData();
                String type = downloadFile.getFileType();
                String name = downloadFile.getFileName();

                return new FileDownloadDto(name,type,content);
            }

        }catch (Exception e){
            System.out.println("could not find file");
        }
        return null;
    }

    public Task checkTask(String taskName) {
        Optional<Task> optionalTaskName = taskRepository.findByTaskName(taskName);
        return optionalTaskName.orElseThrow(() -> new TaskNotFoundException("Task not found. [Task Name Invalid] " + taskName));
    }
}
