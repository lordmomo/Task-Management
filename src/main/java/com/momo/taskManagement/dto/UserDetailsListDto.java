package com.momo.taskManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsListDto {

    private String userId;

    private String firstName;

    private String lastName;

    private String username;

}
