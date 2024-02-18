package com.momo.taskManagement.service.interfaces;

import com.momo.taskManagement.dto.ChangeUserCredentialsDTO;
import com.momo.taskManagement.dto.FileDownloadDto;
import com.momo.taskManagement.dto.UserDto;
import com.momo.taskManagement.exception.UserNotFoundException;
import com.momo.taskManagement.userFolder.model.Task;
import com.momo.taskManagement.userFolder.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    List<UserDto> listOfUsers();
    void updateUser(String username, UserDto userDto);

    ResponseEntity<List<Task>> viewTask(String username);

    String updateCredentials(String username, ChangeUserCredentialsDTO userDto) throws UserNotFoundException;

    FileDownloadDto viewTaskFile(String username) throws UserNotFoundException;

    String removeUser(String username);

    ResponseEntity<User> viewUser(String username) throws UserNotFoundException;

//    List<User> listUsersDesc();

//    List<User> listUsersDescHQL();

}


