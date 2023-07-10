package com.kanban.kanban.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanban.kanban.controller.ProjectController;
import com.kanban.kanban.domain.Project;
import com.kanban.kanban.domain.Task;
import com.kanban.kanban.exception.DuplicateProjectException;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.services.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)

public class ProjectControllerTest {
    @Mock
    ProjectService projectService;
    @InjectMocks
    ProjectController projectController;
    @Autowired
    private MockMvc mockMvc;
    private Project project;

    @BeforeEach
    public void setup() {
        project = new Project();
        project.setName("TestProject");
        project.setMembers(List.of("User1", "User2", "User3"));
        project.setColumns(Map.of("ToDo", List.of(new Task("task1", "test", "High", "16-05-2023", "17-05-2023", "Ezhil", "In Progress", List.of("Ezhil", "Mahek", "Priyanshu")), new Task("TestTask", "test", "Low", "17-05-2023", "18-05-2023", "Mahek", "In Progress", List.of("Ezhil", "Mahek", "Priyanshu"))),
                "Done", List.of(new Task("DoneTask", "test", "Low", "17-05-2023", "20-05-2023", "Priyanshu", "In Progress", List.of("Ezhil", "Mahek", "Priyanshu")))));
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @AfterEach
    public void tearDown() {
        project = null;
    }

    @Test
    public void addProjectTestSuccess() throws Exception {
        when(projectService.createProject(project)).thenReturn(project);
        mockMvc.perform(
                        post("/api/v1/project/add").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(project)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    public void addProjectTestFailure() throws Exception {
        when(projectService.createProject(project)).thenThrow(DuplicateProjectException.class);
        mockMvc.perform(
                        post("/api/v1/project/add").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(project)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    public void getProjectTestSuccess() throws Exception {
        when(projectService.getProject(project.getName())).thenReturn(project);
        mockMvc.perform(
                get("/api/v1/project/TestProject")).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).getProject(project.getName());
    }

    @Test
    public void getProjectTestFailure() throws Exception {
        when(projectService.getProject(project.getName())).thenThrow(ProjectNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/project/TestProject"))
                .andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).getProject(project.getName());
    }

    @Test
    public void updateProjectTestSuccess() throws Exception {
        when(projectService.saveChanges(project.getName(), project.getColumns())).thenReturn(true);
        mockMvc.perform(
                        put("/api/v1/project/save/TestProject").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(project.getColumns())))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).saveChanges(project.getName(), project.getColumns());
    }

    @Test
    public void updateProjectTestFailure() throws Exception {
        when(projectService.saveChanges(project.getName(), project.getColumns())).thenThrow(ProjectNotFoundException.class);
        mockMvc.perform(
                        put("/api/v1/project/save/TestProject").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(project.getColumns())))
                .andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).saveChanges(project.getName(), project.getColumns());
    }

    @Test
    public void deleteProjectTestSuccess() throws Exception {
        when(projectService.deleteProject(project.getName())).thenReturn(true);
        mockMvc.perform(
                        delete("/api/v1/project/delete/TestProject"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).deleteProject(project.getName());
    }

    @Test
    public void deleteProjectTestFailure() throws Exception {
        when(projectService.deleteProject(project.getName())).thenThrow(ProjectNotFoundException.class);
        mockMvc.perform(
                        delete("/api/v1/project/delete/TestProject"))
                .andExpect(status().isNotFound()).andDo(MockMvcResultHandlers.print());
        verify(projectService, times(1)).deleteProject(project.getName());
    }

}
