package com.momo.taskManagement.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record TaskRequestDto(
        String taskName,
        String taskDescription,
        Date taskStartDate,
        Date taskEndDate,
        String taskPriority,
        String taskCategory,
        MultipartFile file
) {
}
