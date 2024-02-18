package com.momo.taskManagement.userFolder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "newtask")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name ="name",nullable = false)
    private String taskName;

    @Column(name ="description",nullable = false)
    private String taskDescription;

    @Temporal(TemporalType.DATE)
    @Column(name ="start_date",nullable = false)
    private Date taskStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name ="end_date" ,nullable = false)
    private Date taskEndDate;

    @Column(name="priority",nullable = false)
    private String taskPriority;

    @Column(name = "category",nullable = false)
    private String taskCategory;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "filetype")
    private String fileType;

    @Lob
    @Column(name = "filedata",columnDefinition = "LONGBLOB")
    private byte[] data;

}
