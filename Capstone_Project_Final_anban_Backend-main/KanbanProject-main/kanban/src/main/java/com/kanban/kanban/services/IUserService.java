package com.kanban.kanban.services;

import com.kanban.kanban.domain.Task;
import com.kanban.kanban.domain.User;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.exception.UserAlreadyExistException;
import com.kanban.kanban.exception.UserNotFoundException;

import java.util.List;

public interface IUserService {
    User registerUser(User user) throws UserAlreadyExistException;

    User userDetails(String userName) throws UserNotFoundException;

    boolean addProjectList(String userName, String projectName) throws UserNotFoundException, ProjectNotFoundException;

    boolean removeProjectList(String userName, String projectName) throws UserNotFoundException, ProjectNotFoundException;

    List<String> getProjectList(String userName) throws UserNotFoundException;

}