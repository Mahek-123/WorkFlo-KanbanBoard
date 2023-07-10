package com.example.finalProject.test;

import com.example.finalProject.domain.Employee;
import com.example.finalProject.exception.EmployeeAlreadyExistException;
import com.example.finalProject.exception.EmployeeNotFoundException;
import com.example.finalProject.repository.EmployeeRepository;
import com.example.finalProject.services.IEmployeeServicesImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private IEmployeeServicesImp iEmployeeServices;

    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = new Employee("Priyanshu", "12345678");
    }

    @AfterEach
    public void tearDown() {
        employee = null;
    }

    @Test
    public void addEmployeeSuccess() throws EmployeeAlreadyExistException {
        Employee employee1 = new Employee("negi", "1234");
        when(employeeRepository.findById(employee1.getUserName())).thenReturn(Optional.ofNullable(null));
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        Employee addedEmployee = iEmployeeServices.addEmployee(employee1);
        Assertions.assertEquals(employee1, addedEmployee);
        verify(employeeRepository, times(1)).findById(employee1.getUserName());
        verify(employeeRepository, times(1)).save(employee1);
    }


    @Test
    public void addEmployeeFailure() throws EmployeeAlreadyExistException {
        when(employeeRepository.findById(employee.getUserName())).thenReturn(Optional.ofNullable(employee));
        assertThrows(EmployeeAlreadyExistException.class, () -> iEmployeeServices.addEmployee(employee));
    }

    @Test
    public void getEmployeeByNameSuccess() throws EmployeeNotFoundException {
        when(employeeRepository.findById(employee.getUserName())).thenReturn(Optional.of(employee));
        when(employeeRepository.findByUserNameAndPassword(employee.getUserName(), employee.getPassword())).thenReturn(employee);
        Employee fetchedEmployee = iEmployeeServices.getEmployee(employee);
        Assertions.assertEquals("Priyanshu", fetchedEmployee.getUserName());
        Assertions.assertEquals("12345678", fetchedEmployee.getPassword());
        verify(employeeRepository, times(1)).findById(employee.getUserName());
        verify(employeeRepository, times(1)).findByUserNameAndPassword(employee.getUserName(), employee.getPassword());
    }

    @Test
    public void getEmployeeByNameFailure() {
        when(employeeRepository.findById(employee.getUserName())).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> iEmployeeServices.getEmployee(employee));
        verify(employeeRepository, times(1)).findById(employee.getUserName());
    }

    @Test
    public void getEmployeeByNameSuccessTest() {
        when(employeeRepository.findById(employee.getUserName())).thenReturn(Optional.ofNullable(employee));
        Boolean status = iEmployeeServices.getEmployeeByName(employee.getUserName());
        Assertions.assertEquals(true, status);
        verify(employeeRepository, times(1)).findById(employee.getUserName());
    }

    @Test
    public void getEmployeeByNameFailureTest() {
        when(employeeRepository.findById(employee.getUserName())).thenReturn(Optional.ofNullable(null));
        Boolean status = iEmployeeServices.getEmployeeByName(employee.getUserName());
        Assertions.assertEquals(false, status);
        verify(employeeRepository, times(1)).findById(employee.getUserName());
    }
}