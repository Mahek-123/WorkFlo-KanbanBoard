package com.example.finalProject.services;

import com.example.finalProject.domain.Employee;
import com.example.finalProject.exception.EmployeeAlreadyExistException;
import com.example.finalProject.exception.EmployeeNotFoundException;
import com.example.finalProject.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IEmployeeServicesImp implements IEmployeeServices {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee addEmployee(Employee employee) throws EmployeeAlreadyExistException {
        if (employeeRepository.findById(employee.getUserName()).isEmpty()) {
            return employeeRepository.save(employee);
        } else {
            throw new EmployeeAlreadyExistException();
        }
    }

    @Override
    public Employee getEmployee(Employee employee) throws EmployeeNotFoundException {
        {
            if (employeeRepository.findById(employee.getUserName()).isEmpty()) {
                throw new EmployeeNotFoundException();
            }
            return employeeRepository.findByUserNameAndPassword(employee.getUserName(), employee.getPassword());
        }
    }

    @Override
    public boolean getEmployeeByName(String name) {
        if (employeeRepository.findById(name).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

}