package com.example.finalProject.test;

import com.example.finalProject.controller.EmployeeController;
import com.example.finalProject.domain.Employee;
import com.example.finalProject.domain.EmployeeDTO;
import com.example.finalProject.exception.EmployeeAlreadyExistException;
import com.example.finalProject.exception.EmployeeNotFoundException;
import com.example.finalProject.services.IEmployeeServices;
import com.example.finalProject.services.ISecurityTokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ControllerTest {
    @Mock
    private IEmployeeServices employeeServices;
    @Mock
    private ISecurityTokenGenerator securityTokenGenerator;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void addNewUserSuccess() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("Priyanshu", "password");
        Employee employee = new Employee(employeeDTO.getUserName(), employeeDTO.getPassword());
        when(employeeServices.addEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/v1/auth/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employeeDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void addNewUserThrowsEmployeeAlreadyExistException() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO("Priyanshu", "password");
        when(employeeServices.addEmployee(any(Employee.class))).thenThrow(new EmployeeAlreadyExistException());

        mockMvc.perform(post("/api/v1/auth/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employeeDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    void loginCustomerSuccess() throws Exception {
        Employee employee = new Employee("Priyanshu", "password");
        Employee fetchedEmployee = new Employee("Priyanshu", "password");

        when(employeeServices.getEmployee(any(Employee.class))).thenReturn(fetchedEmployee);

        // Mock the ISecurityTokenGenerator interface
        ISecurityTokenGenerator securityTokenGenerator = Mockito.mock(ISecurityTokenGenerator.class);
        Map<String, String> mockToken = new HashMap<>();
        mockToken.put("Token", "mock-token-value");
        mockToken.put("Message", "Employee loggedIn Successfully");
        when(securityTokenGenerator.generateToken(any(Employee.class))).thenReturn(mockToken);

        // Create an instance of the EmployeeController and manually inject the mocked dependencies
        EmployeeController employeeController = new EmployeeController();
        employeeController.setIEmployeeServices(employeeServices);
        employeeController.setISecurityTokenGenerator(securityTokenGenerator);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employee)))
                .andExpect(status().isOk());
    }

    @Test
    void loginCustomerThrowsEmployeeNotFoundException() throws Exception {
        Employee employee = new Employee("Priyanshu", "password");
        when(employeeServices.getEmployee(any(Employee.class))).thenThrow(new EmployeeNotFoundException());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(employee)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The Authentication was failed"));
    }


    @Test
    void getEmployeeByNameSuccess() throws Exception {

        when(employeeServices.getEmployeeByName("Priyanshu")).thenReturn(false);

        mockMvc.perform(get("/api/v1/auth/findUser/Priyanshu"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeByNameFailure() throws Exception {
        when(employeeServices.getEmployeeByName("Priyanshu")).thenReturn(false);

        mockMvc.perform(get("/api/v1/auth/findUser/Priyanshu"))
                .andExpect(status().isOk());
    }

    public String convertToJson(final Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}