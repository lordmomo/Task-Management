package com.momo.taskManagement.service.impl;

import com.momo.taskManagement.dto.ChangeUserCredentialsDTO;
import com.momo.taskManagement.dto.FileDownloadDto;
import com.momo.taskManagement.dto.UserDto;
import com.momo.taskManagement.exception.UserNotFoundException;
import com.momo.taskManagement.userFolder.model.Task;
import com.momo.taskManagement.userFolder.model.User;
import com.momo.taskManagement.userFolder.repository.UserRepository;
import com.momo.taskManagement.service.interfaces.TaskService;
import com.momo.taskManagement.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskService taskService;

    @Autowired
    PasswordEncoder passwordEncoder;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public List<UserDto> listOfUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(modelMapper.map(user, UserDto.class));
        }
        return userDtoList;
    }

    @Override
    public void updateUser(String username, UserDto userDto) {

        User updtUser = checkUser(username);

        if (Objects.nonNull(userDto.getFirstName()) && !"".equalsIgnoreCase(userDto.getFirstName())) {
            updtUser.setFirstName(userDto.getFirstName());
        }
        if (Objects.nonNull(userDto.getLastName()) && !"".equalsIgnoreCase(userDto.getLastName())) {
            updtUser.setLastName(userDto.getLastName());
        }
        if (Objects.nonNull(userDto.getEmail()) && !"".equalsIgnoreCase(userDto.getEmail())) {
            updtUser.setEmail(userDto.getEmail());
        }
        userRepository.save(updtUser);

    }

    @Override
    public ResponseEntity<List<Task>> viewTask(String username) {

        User user = checkUser(username);
        Long userTaskId = user.getTaskId().getId();
        return taskService.showDetails(userTaskId);

    }

    @Override
    public String updateCredentials(String username, ChangeUserCredentialsDTO userDto) {

        User user = checkUser(username);

        if (Objects.nonNull(userDto.getUsername()) && !"".equalsIgnoreCase(userDto.getUsername())) {
            user.setUsername(userDto.getUsername());
        }
        if (Objects.nonNull(userDto.getPassword()) && !"".equalsIgnoreCase(userDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(user);
        return "User Credentials Updated";

    }

    @Override
    public FileDownloadDto viewTaskFile(String username) {
        User user = checkUser(username);
        var userTaskId = user.getTaskId().getId();
        return taskService.downloadFile(userTaskId);

//        Optional<User> optionalUser = userRepository.findByUsername(username);
//        if(optionalUser.isPresent()){
//            User user = optionalUser.get();
//            var userTaskId = user.getTaskId().getId();
//            return taskService.downloadFile(userTaskId);
//        }
//        return null;
    }

    @Override
    public String removeUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return "User successfully removed";
        } else {
            return "User not found";
        }
    }

    @Override
    public ResponseEntity<User> viewUser(String username) {
        User user = checkUser(username);
        return ResponseEntity.ok(user);
//            Optional<User> optionalUser = userRepository.findByUsername(username);
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                return ResponseEntity.ok(user);
//            }
//            throw new UserNotFoundException("User not found: "+username);
    }

//    @Override
//    public List<User> listUsersDesc() {
//        log.info("Inside userListDec controller");
//        return userRepository.findAllUserDesc();
//    }

//    @Override
//    public List<User> listUsersDescHQL() {
//        return userRepository.findAllUserDescHQL();
//    }

    public User checkUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }
}
