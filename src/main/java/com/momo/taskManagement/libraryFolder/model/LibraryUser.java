package com.momo.taskManagement.libraryFolder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "libUser")
public class LibraryUser {
    @Id
    @Column(nullable = false,unique = true)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false,unique = true)
    private int number;
}



