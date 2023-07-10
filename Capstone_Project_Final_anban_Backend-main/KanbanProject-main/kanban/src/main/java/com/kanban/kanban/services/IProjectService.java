package com.kanban.kanban.services;

import com.kanban.kanban.domain.Project;
import com.kanban.kanban.domain.Task;
import com.kanban.kanban.exception.DuplicateProjectException;
import com.kanban.kanban.exception.ProjectNotFoundException;

import java.util.List;
import java.util.Map;

public interface IProjectService {
    Project createProject(Project project) throws DuplicateProjectException;

    Project getProject(String name) throws ProjectNotFoundException;

    boolean deleteProject(String name) throws ProjectNotFoundException;

    boolean saveChanges(String name, Map<String, List<Task>> columns) throws ProjectNotFoundException;

    Project addNewTask(String name, Task task);

    boolean deleteMemeberFromProject(String projectName, String userName) throws ProjectNotFoundException;

    Project editProject(String name, Project project) throws ProjectNotFoundException;


}
