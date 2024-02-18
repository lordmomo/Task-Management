package com.momo.taskManagement.dto;

public record FileDownloadDto (String name,
                              String type,
                              byte[] content){
}
