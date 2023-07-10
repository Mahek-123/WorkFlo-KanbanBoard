package com.kanban.kanban.user;

import com.kanban.kanban.domain.User;
import com.kanban.kanban.repository.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private IUserRepository iUserRepository;
    private User user;

    @BeforeEach
    public void setup() {
        List<String> projectList = new ArrayList<String>();
        projectList.add("Project1");
        projectList.add("Project2");
        user = new User("sample", "s@123.com", "sample234", "8987898789", projectList);
    }

    @AfterEach
    public void remove() {
        user = null;
        iUserRepository.deleteAll();
    }

    @Test
    public void registerUserTest() {
        iUserRepository.insert(user);
        boolean registered = iUserRepository.findById(user.getName()).isPresent();
        assertEquals(true, registered);
    }

    @Test
    public void userDetailsTest() {
        iUserRepository.insert(user);
        iUserRepository.findById(user.getName());
        boolean userExist = iUserRepository.findById(user.getName()).isPresent();
        assertEquals(true, userExist);
    }

    @Test
    public void addProjectListTest() {
        iUserRepository.insert(user);
        User usr = iUserRepository.findById(user.getName()).get();
        List<String> listUsr = usr.getProjectList();
        listUsr.add("Project3");
        usr.setProjectList(listUsr);
        iUserRepository.save(usr);

        boolean added = usr.getProjectList().contains("Project3");
        assertEquals(true, added);
    }

    @Test
    public void removeProjectListTest() {
        iUserRepository.insert(user);
        User usr = iUserRepository.findById(user.getName()).get();
        List<String> listUsr = usr.getProjectList();
        listUsr.remove("Project 3");
        usr.setProjectList(listUsr);
        iUserRepository.save(usr);

        boolean removed = usr.getProjectList().contains("Project 3");
        assertFalse(removed);
    }

    @Test
    public void getProjectListTest() {
        iUserRepository.insert(user);
        List<String> prjList = iUserRepository.findById(user.getName()).get().getProjectList();
        assertEquals(user.getProjectList(), prjList);
    }
}