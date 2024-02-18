package com.momo.taskManagement.controller;

import com.momo.taskManagement.dto.ChangeUserCredentialsDTO;
import com.momo.taskManagement.dto.FileDownloadDto;
import com.momo.taskManagement.dto.UserDetailsListDto;
import com.momo.taskManagement.dto.UserDto;
import com.momo.taskManagement.exception.UserNotFoundException;
import com.momo.taskManagement.libraryFolder.model.LibraryUser;
import com.momo.taskManagement.libraryFolder.repository.LibraryUserRespository;
import com.momo.taskManagement.service.impl.UserDetailsUsingEntityManager;
import com.momo.taskManagement.service.interfaces.UserService;
import com.momo.taskManagement.userFolder.model.Task;
import com.momo.taskManagement.userFolder.model.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    HttpServletResponse response;

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserDetailsUsingEntityManager userDetailsUsingEntityManager;

    @Autowired
    LibraryUserRespository libraryUserRespository;

    @GetMapping("/view-user/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> viewUser(@PathVariable("username") String username) throws UserNotFoundException {
        return userService.viewUser(username);
    }

    @GetMapping("/view-all-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> showAllUsers() {
        return userService.listOfUsers();
    }

    @PutMapping("/update-user/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #username == authentication.name) ")
    public ResponseEntity<String> updateUserDetails(@PathVariable("username") String username, UserDto userDto) {
        userService.updateUser(username, userDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Update successful");
    }

    @PutMapping("/update-user-credentials/{username}")
    @PreAuthorize("#username == authentication.name")
    public ResponseEntity<String> changeUserCredentials(@PathVariable("username") String username, @RequestBody ChangeUserCredentialsDTO userDto) {
        String response = userService.updateCredentials(username, userDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/remove-user/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> removeUser(@PathVariable String username) {
        String message = userService.removeUser(username);
        return ResponseEntity.ok(message);
    }


    @GetMapping("/{username}/view-task")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<Task>> viewTask(@PathVariable("username") String username) {
        return userService.viewTask(username);
    }

    @GetMapping("/view-task-file/{username}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #username == authentication.name )")
    public String viewTaskFile(@PathVariable("username") String username) throws IOException {
        FileDownloadDto file = userService.viewTaskFile(username);
        if (file != null) {
            byte[] content = file.content();
            if (content != null) {
                response.reset();
                response.setContentType(file.type());
                response.setHeader("Content-Disposition", "attachment;        filename=" + file.name());
                BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
                outStream.write(content);
                outStream.close();
                return "File retrieved successfully";
            } else {
                return "Task hasn't been provided with a file";
            }
        } else {
            return "File doesn't exist";
        }
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<String> welcomeMessage() {
        String message = messageSource.getMessage("hello.message", null, LocaleContextHolder.getLocale());
        log.info(message);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(message);
    }

//    @GetMapping("/user-list-desc")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public List<User> userListDesc (){
//        log.info("Inside userListDec controller");
//        return userService.listUsersDesc();
//    }

    @GetMapping("/user-list-dto")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDetailsListDto> userListDto() {
        return userDetailsUsingEntityManager.getUserDetailDesc();
    }

//    @GetMapping("/user-list-desc-hql")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public List<User> userListDescHQL (){
//        return userService.listUsersDescHQL();
//    }

    //entity manager, persistentcontext


    @GetMapping("/library-list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<LibraryUser> viewLibrary() {
        return libraryUserRespository.findAll();
    }
}
