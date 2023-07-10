package com.kanban.kanban.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanban.kanban.controller.UserController;
import com.kanban.kanban.domain.User;
import com.kanban.kanban.exception.UserAlreadyExistException;
import com.kanban.kanban.exception.UserNotFoundException;
import com.kanban.kanban.services.IUserService;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private IUserService iUserService;

    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private User user;

    @BeforeEach
    public void setup() {
        List<String> projectList = new ArrayList<String>();
        projectList.add("Project1");
        projectList.add("Project2");
        user = new User("sample", "s@123.com", "sample234", "8987898789", projectList);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterEach
    public void remove() {
        user = null;
    }


    public static String convertToJson(final Object object) {
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Test
    public void registerUserSuccess() throws Exception {
        when(iUserService.registerUser(user)).thenReturn(user);
        mockMvc.perform(
                        post("/api/v1/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(user)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(1)).registerUser(user);
    }

    @Test
    public void registerUserFailure() throws Exception {
        when(iUserService.registerUser(user)).thenThrow(UserAlreadyExistException.class);
        mockMvc.perform(
                        post("/api/v1/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertToJson(user)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(1)).registerUser(user);
    }

    @Test
    public void userDetailsSuccess() throws Exception {
        when(iUserService.userDetails(user.getName())).thenReturn(user);
        mockMvc.perform(get("/api/v1/user/details").requestAttr("attr1", user.getName()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(1)).userDetails(user.getName());
    }

    @Test
    public void userDetailsFailure() throws Exception {
        when(iUserService.userDetails(user.getName())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/user/details").requestAttr("attr1", user.getName()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(1)).userDetails(user.getName());

    }

    @Test
    public void addProjectSuccess() throws Exception {
        when(iUserService.addProjectList(user.getName(), "Project3")).thenReturn(true);
        mockMvc.perform(get("/api/v1/user/addProject/Project3").requestAttr("attr1", user.getName()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).userDetails(user.getName());
    }

    @Test
    public void addProjectFailure() throws Exception {
        when(iUserService.addProjectList(user.getName(), "Project3")).thenThrow(UserNotFoundException.class);
        mockMvc.perform(get("/api/v1/user/addProject/Project3").requestAttr("attr1", user.getName()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).userDetails(user.getName());
    }


    @Test
    public void updateProjectSuccess() throws Exception {
        when(iUserService.addProjectList(user.getName(), "Project2")).thenReturn(true);
        mockMvc.perform(
                        get("/api/v1/user/updateProject/sample/Project2"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).registerUser(user);

    }

    @Test
    public void updateProjectFailure() throws Exception {
        when(iUserService.addProjectList(user.getName(), "Project2")).thenThrow(UserNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/user/updateProject/sample/Project2"))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).registerUser(user);

    }

    @Test
    public void removeProjectSuccess() throws Exception {
        when(iUserService.removeProjectList(user.getName(), "Project1")).thenReturn(true);
        mockMvc.perform(
                        get("/api/v1/user/removeProject/Project1").requestAttr("attr1", user.getName()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).registerUser(user);
    }

    @Test
    public void removeProjectFailure() throws Exception {
        when(iUserService.removeProjectList(user.getName(), "Project1")).thenThrow(UserNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/user/removeProject/Project1").requestAttr("attr1", user.getName()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).registerUser(user);
    }

    @Test
    public void getProjectListSuccess() throws Exception {
        List<String> projectList = new ArrayList<>();
        when(iUserService.getProjectList(user.getName())).thenReturn(projectList);
        mockMvc.perform(
                        get("/api/v1/user/projectList").requestAttr("attr1", user.getName()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).registerUser(user);
    }

    @Test
    public void getProjectListFailure() throws Exception {
        when(iUserService.getProjectList(user.getName())).thenThrow(UserNotFoundException.class);
        mockMvc.perform(
                        get("/api/v1/user/projectList").requestAttr("attr1", user.getName()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(iUserService, times(0)).registerUser(user);
    }


}