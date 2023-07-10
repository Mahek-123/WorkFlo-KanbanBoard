package com.kanban.kanban.project;

import com.kanban.kanban.domain.Project;
import com.kanban.kanban.domain.Task;
import com.kanban.kanban.exception.DuplicateProjectException;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.repository.IProjectRepository;
import com.kanban.kanban.services.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    private IProjectRepository projectRepository;
    @InjectMocks
    private ProjectService projectService;

    private Project project;

    @BeforeEach
    public void setup() {
        project = new Project();
        project.setName("TestProject");
        project.setMembers(List.of("User1", "User2", "User3"));
        project.setColumns(Map.of("ToDo", List.of(new Task("task1", "test", "High", "16-05-2023", "17-05-2023", "Ezhil", "In Progress", List.of("Ezhil", "Mahek", "Priyanshu")), new Task("TestTask", "test", "Low", "17-05-2023", "18-05-2023", "Mahek", "In Progress", List.of("Ezhil", "Mahek", "Priyanshu"))),
                "Done", List.of(new Task("DoneTask", "test", "Low", "17-05-2023", "20-05-2023", "Priyanshu", "In Progress", List.of("Ezhil", "Mahek", "Priyanshu")))));
    }

    @AfterEach
    public void tearDown() {
        project = null;
    }
    @Test
    public void createProjectTestSuccess() throws DuplicateProjectException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(null));
        when(projectRepository.insert(project)).thenReturn(project);
        Project project1 = projectService.createProject(project);
        verify(projectRepository,times(1)).findById(project.getName());
        verify(projectRepository,times(1)).insert(project);
    }
    @Test
    public void createProjectTestFail() throws DuplicateProjectException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(project));
        assertThrows(DuplicateProjectException.class,()->projectService.createProject(project));
        verify(projectRepository,times(1)).findById(project.getName());
        verify(projectRepository,times(0)).insert(project);
    }
    @Test
    public void getProjectTestSuccess() throws ProjectNotFoundException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(project));
        Project project1 = projectService.getProject(project.getName());
        verify(projectRepository,times(2)).findById(project.getName());
    }
    @Test
    public void getProjectTestFail() throws ProjectNotFoundException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(null));
        assertThrows(ProjectNotFoundException.class,()->projectService.getProject(project.getName()));
        verify(projectRepository,times(1)).findById(project.getName());
    }
    @Test
    public void deleteProjectTestSuccess() throws ProjectNotFoundException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(project));
        assertTrue(projectService.deleteProject(project.getName()));
        verify(projectRepository,times(1)).findById(project.getName());
        verify(projectRepository,times(1)).deleteById(project.getName());
    }
    @Test
    public void deleteProjectTestFail() throws ProjectNotFoundException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(null));
        assertThrows(ProjectNotFoundException.class,()->projectService.deleteProject(project.getName()));
        verify(projectRepository,times(1)).findById(project.getName());
        verify(projectRepository,times(0)).deleteById(project.getName());
    }
    @Test
    public void saveChangesTestSuccess() throws ProjectNotFoundException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(project));
        when(projectRepository.save(project)).thenReturn(project);
        assertTrue(projectService.saveChanges(project.getName(),project.getColumns()));
        verify(projectRepository,times(2)).findById(project.getName());
        verify(projectRepository,times(1)).save(project);
    }
    @Test
    public void saveChangesTestFail() throws ProjectNotFoundException {
        when(projectRepository.findById(project.getName())).thenReturn(Optional.ofNullable(null));
        assertThrows(ProjectNotFoundException.class,()->projectService.saveChanges(project.getName(),project.getColumns()));
        verify(projectRepository,times(1)).findById(project.getName());
        verify(projectRepository,times(0)).save(project);
    }
}
