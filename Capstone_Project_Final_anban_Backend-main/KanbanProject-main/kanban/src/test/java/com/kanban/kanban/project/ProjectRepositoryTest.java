package com.kanban.kanban.project;

import com.kanban.kanban.domain.Project;
import com.kanban.kanban.domain.Task;
import com.kanban.kanban.repository.IProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest

public class ProjectRepositoryTest {
    @Autowired
    IProjectRepository projectRepository;

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
        projectRepository.deleteAll();
    }

    @Test
    public void insertProjectTestSuccess() {
        Project project1 = projectRepository.insert(project);
        assertEquals(project, project1);
    }

    @Test
    public void insertProjectTestFail() {
        projectRepository.insert(project);
        assertThrows(DuplicateKeyException.class, () -> projectRepository.insert(project));
    }

    @Test
    public void findByIdProjectTestSuccess() {
        projectRepository.insert(project);
        Project project1 = projectRepository.findById("TestProject").get();
        assertEquals(project, project1);
    }

    @Test
    public void findByIdProjectTestFail() {
        assertFalse(projectRepository.findById("test").isPresent());
    }

    @Test
    public void deleteByIdTestSuccess() {
        projectRepository.insert(project);
        assertTrue(projectRepository.findById(project.getName()).isPresent());
        projectRepository.deleteById(project.getName());
        assertFalse(projectRepository.findById(project.getName()).isPresent());
    }

    @Test
    public void deleteByIdTestFail() {
        assertFalse(projectRepository.findById(project.getName()).isPresent());
        projectRepository.deleteById(project.getName());
        assertFalse(projectRepository.findById(project.getName()).isPresent());
    }
    @Test
    public void saveProjectTestSuccess(){
        Project project1 = projectRepository.save(project);
        assertEquals(project,project1);
        project.setName("NewName");
        Project project2 = projectRepository.save(project);
        assertEquals(project,project2);
    }
//    @Test
//    public void saveProjectTestFailure(){
//        Project project1 = projectRepository.save(project);
//        assertEquals(project,project1);
//        project1.setName("NewName");
//        Project project2 = projectRepository.save(project1);
//        assertEquals(project,project2);
//    }



}
