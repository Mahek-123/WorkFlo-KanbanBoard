package com.kanban.kanban.user;

import com.kanban.kanban.domain.EmployeeDTO;
import com.kanban.kanban.domain.User;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.exception.UserAlreadyExistException;
import com.kanban.kanban.exception.UserNotFoundException;
import com.kanban.kanban.proxy.NotificationProxy;
import com.kanban.kanban.proxy.ProjectProxy;
import com.kanban.kanban.proxy.UserProxy;
import com.kanban.kanban.repository.IUserRepository;
import com.kanban.kanban.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository iUserRepository;

    @Mock
    private UserProxy userProxy;
    @InjectMocks
    private UserService iUserService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private DirectExchange directExchange;

    @Mock
    private ProjectProxy projectProxy;

    @Mock
    private NotificationProxy notificationProxy;
    private User user;
    private EmployeeDTO employeeDTO;

    private UserService userService;
    @BeforeEach
    public void setup() {
        List<String> projectList = new ArrayList<String>();
        projectList.add("Project1");
        projectList.add("Project2");
        user = new User("sample", "s@123.com", "sample234", "8987898789", projectList);
        employeeDTO = new EmployeeDTO("sample", "sample234");

        MockitoAnnotations.openMocks(this);
        userService = new UserService(iUserRepository, userProxy, rabbitTemplate, directExchange, projectProxy, notificationProxy);

    }

    @AfterEach
    public void remove() {
        user = null;
        employeeDTO = null;
    }

    @Test
    public void registerUserSuccess() throws UserAlreadyExistException {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.ofNullable(null));
        ResponseEntity response = new ResponseEntity(employeeDTO, HttpStatus.OK);
        when(iUserRepository.insert(user)).thenReturn(user);
        when(userProxy.addNewUser(employeeDTO)).thenReturn(response);
        User user1 = iUserService.registerUser(user);

        assertEquals(user1, user);
        verify(userProxy, times(1)).addNewUser(employeeDTO);
        verify(iUserRepository, times(1)).findById(user.getName());
        verify(iUserRepository, times(1)).insert(user);
    }

    @Test
    public void registerUserFailure() {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistException.class, () -> iUserService.registerUser(user));
        verify(iUserRepository, times(1)).findById(user.getName());
        verify(iUserRepository, times(0)).insert(user);
    }

    @Test
    public void userDetailsSuccess() throws UserNotFoundException {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.ofNullable(user));

        User user1 = iUserService.userDetails(user.getName());
        assertEquals(user, user1);

        verify(iUserRepository, times(2)).findById(user.getName());
    }

    @Test
    public void userDetailsFailure() {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class, () -> iUserService.userDetails(user.getName()));
        verify(iUserRepository, times(1)).findById(user.getName());
    }

    @Test
    public void addProjectListSuccess() throws UserNotFoundException, ProjectNotFoundException {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.of(user));
        when(iUserRepository.save(user)).thenReturn(user);
        iUserService.addProjectList(user.getName(), "Project3");
        int noOfProjects = user.getProjectList().size();
        assertEquals(3, noOfProjects);

        verify(iUserRepository, times(2)).findById(user.getName());
        verify(iUserRepository, times(1)).save(user);
    }

    @Test
    public void addProjectListFailure() {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class, () -> iUserService.addProjectList(user.getName(), "Project3"));
        verify(iUserRepository, times(1)).findById(user.getName());
    }

    @Test
    public void removeProjectListSuccess() throws UserNotFoundException, ProjectNotFoundException {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.of(user));
        when(iUserRepository.save(user)).thenReturn(user);

        iUserService.removeProjectList(user.getName(), "Project2");

        int noOfProjects = user.getProjectList().size();
        assertEquals(1, noOfProjects);
        verify(iUserRepository, times(2)).findById(user.getName());
        verify(iUserRepository, times(1)).save(user);
    }

    @Test
    public void removeProjectListFailure() {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class, () -> iUserService.removeProjectList(user.getName(), "Project2"));
        verify(iUserRepository, times(1)).findById(user.getName());
    }

    @Test
    public void getProjectListSuccess() throws UserNotFoundException {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.of(user));

        int projects = iUserService.getProjectList(user.getName()).size();
        assertEquals(2, projects);
        verify(iUserRepository, times(2)).findById(user.getName());
    }

    @Test
    public void getProjectListFailure() {
        when(iUserRepository.findById(user.getName())).thenReturn(Optional.ofNullable(null));

        assertThrows(UserNotFoundException.class,()->iUserService.getProjectList(user.getName()));
        verify(iUserRepository,times(1)).findById(user.getName());
    }

}