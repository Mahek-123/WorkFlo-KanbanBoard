package com.example.finalProject.controller;

import com.example.finalProject.domain.Employee;
import com.example.finalProject.domain.EmployeeDTO;
import com.example.finalProject.exception.EmployeeAlreadyExistException;
import com.example.finalProject.exception.EmployeeNotFoundException;
import com.example.finalProject.services.IEmployeeServices;
import com.example.finalProject.services.ISecurityTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class EmployeeController {
    @Autowired
    private IEmployeeServices iEmployeeServices;
    @Autowired
    private ISecurityTokenGenerator iSecurityTokenGenerator;

//     http://localhost:3033/api/v1/auth/addUser
    @PostMapping("/addUser")
    public ResponseEntity<?> addNewUser(@RequestBody EmployeeDTO employeeDTO) throws EmployeeAlreadyExistException {
        Employee employee = new Employee(employeeDTO.getUserName(), employeeDTO.getPassword());
        return new ResponseEntity<>(iEmployeeServices.addEmployee(employee), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Employee employee) throws EmployeeNotFoundException {
        try {
            Employee fetchedCustomer = iEmployeeServices.getEmployee(employee);
            if (fetchedCustomer != null) {
                return new ResponseEntity<>(iSecurityTokenGenerator.generateToken(fetchedCustomer), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("The Authentication was failed", HttpStatus.EXPECTATION_FAILED);
            }
        } catch (EmployeeNotFoundException e) {
            return new ResponseEntity<>("The Authentication was failed", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findUser/{name}")
    public ResponseEntity<?> getEmployeeByName(@PathVariable String name) {
        return new ResponseEntity<>(iEmployeeServices.getEmployeeByName(name), HttpStatus.OK);
    }

    //    Method for the testing purpose
    public void setISecurityTokenGenerator(ISecurityTokenGenerator securityTokenGenerator) {
        this.iSecurityTokenGenerator = securityTokenGenerator;
    }

    public void setIEmployeeServices(IEmployeeServices employeeServices) {
        this.iEmployeeServices = employeeServices;
    }
}