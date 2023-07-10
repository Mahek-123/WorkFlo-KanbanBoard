package com.kanban.kanban.controller;

import com.kanban.kanban.domain.Project;
import com.kanban.kanban.domain.Task;
import com.kanban.kanban.exception.DuplicateProjectException;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/project")
public class ProjectController {
    @Autowired
    IProjectService projectService;

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody Project project) throws DuplicateProjectException {
        return new ResponseEntity<>(projectService.createProject(project), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getProject(@PathVariable String name) throws ProjectNotFoundException {
        return new ResponseEntity<>(projectService.getProject(name), HttpStatus.OK);
    }

    @PutMapping("/save/{name}")
    public ResponseEntity<?> updateProject(@PathVariable String name, @RequestBody Map<String, List<Task>> columns) throws ProjectNotFoundException {
        return new ResponseEntity<>(projectService.saveChanges(name, columns), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteProject(@PathVariable String name) throws ProjectNotFoundException {
        return new ResponseEntity<>(projectService.deleteProject(name), HttpStatus.OK);
    }

    @PutMapping("/task/{name}")
    public ResponseEntity<?> addnewTask(@PathVariable String name, @RequestBody Task task) {
        return new ResponseEntity<>(projectService.addNewTask(name, task), HttpStatus.OK);
    }

    @GetMapping("/deleteMember/{projectName}/{userName}")
    public ResponseEntity<?> deleteMemberOfProject(@PathVariable String projectName, @PathVariable String userName) throws ProjectNotFoundException {

        return new ResponseEntity<>(projectService.deleteMemeberFromProject(projectName, userName), HttpStatus.OK);
    }

    @PutMapping("/editProject/{name}")
    public ResponseEntity<?> editProject(@PathVariable String name, @RequestBody Project project) throws ProjectNotFoundException {
        return new ResponseEntity(projectService.editProject(name, project), HttpStatus.OK);
    }
}
