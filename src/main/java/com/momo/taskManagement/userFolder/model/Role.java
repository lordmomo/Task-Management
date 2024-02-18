package com.momo.taskManagement.userFolder.model;

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
@Table(name = "newrole")
public class Role {

    @Id
    @Column(name = "id",nullable = false)
    String roleId;
    @Column(name = "name",nullable = false)
    String roleName;
}
