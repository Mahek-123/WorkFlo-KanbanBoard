package com.kanban.kanban.controller;

import com.kanban.kanban.domain.User;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.exception.UserAlreadyExistException;
import com.kanban.kanban.exception.UserNotFoundException;
import com.kanban.kanban.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    IUserService userService;

    // http://localhost:8007/api/v1/user/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) throws UserAlreadyExistException {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/details")
    public ResponseEntity<?> userDetails(HttpServletRequest httpServletRequest) throws UserNotFoundException {
        String username = (String) httpServletRequest.getAttribute("attr1");
        return new ResponseEntity<>(userService.userDetails(username), HttpStatus.OK);
    }

    @GetMapping("/addProject/{projectName}")
    public ResponseEntity<?> addProject(@PathVariable String projectName, HttpServletRequest httpServletRequest) throws UserNotFoundException, ProjectNotFoundException {
        String username = (String) httpServletRequest.getAttribute("attr1");
        return new ResponseEntity<>(userService.addProjectList(username, projectName), HttpStatus.OK);
    }

    @GetMapping("/updateProject/{username}/{projectName}")
    public ResponseEntity<?> addProject(@PathVariable String projectName, @PathVariable String username) throws UserNotFoundException, ProjectNotFoundException {
        return new ResponseEntity<>(userService.addProjectList(username, projectName), HttpStatus.OK);
    }

    @GetMapping("/removeProject/{projectName}")
    public ResponseEntity<?> removeProject(@PathVariable String projectName, HttpServletRequest httpServletRequest) throws UserNotFoundException, ProjectNotFoundException {
        String username = (String) httpServletRequest.getAttribute("attr1");
        return new ResponseEntity<>(userService.removeProjectList(username, projectName), HttpStatus.OK);
    }

    @GetMapping("/projectList")
    public ResponseEntity<?> getProjectList(HttpServletRequest httpServletRequest) throws UserNotFoundException {
        String username = (String) httpServletRequest.getAttribute("attr1");
        return new ResponseEntity<>(userService.getProjectList(username), HttpStatus.OK);
    }

    @GetMapping("/removeProjectFromMember/{projectName}/{name}")
    public ResponseEntity<?> removeProjectOfUser(@PathVariable String projectName,@PathVariable String name) throws UserNotFoundException, ProjectNotFoundException {
         return new ResponseEntity<>(userService.removeProjectList(name, projectName), HttpStatus.OK);
    }
}