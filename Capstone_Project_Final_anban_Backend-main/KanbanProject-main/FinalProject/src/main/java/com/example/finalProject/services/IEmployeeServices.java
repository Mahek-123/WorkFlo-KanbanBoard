package com.example.finalProject.services;

import com.example.finalProject.domain.Employee;
import com.example.finalProject.exception.EmployeeAlreadyExistException;
import com.example.finalProject.exception.EmployeeNotFoundException;

public interface IEmployeeServices {
    Employee addEmployee(Employee employee) throws EmployeeAlreadyExistException;

    Employee getEmployee(Employee employee) throws EmployeeNotFoundException;

    boolean getEmployeeByName(String name);

}