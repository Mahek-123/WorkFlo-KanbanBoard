package com.example.finalProject.test;

import com.example.finalProject.domain.Employee;
import com.example.finalProject.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

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
    public void testAddSuccess() {
        Assertions.assertEquals(employeeRepository.save(employee), employee);
    }

    @Test
    public void testFindByNameAndPassword() {
        employeeRepository.save(employee);
        Assertions.assertEquals(employee, employeeRepository.findByUserNameAndPassword(employee.getUserName(), employee.getPassword()));

    }

    @Test
    public void testFindByNameAndPasswordFailure() {
        employeeRepository.save(employee);
        Assertions.assertNull(employeeRepository.findByUserNameAndPassword("IncorrectUsername", employee.getPassword()));

        Assertions.assertNull(employeeRepository.findByUserNameAndPassword(employee.getUserName(), "IncorrectPassword"));
    }

    @Test
    public void testFindByID() {
        Employee savedEmployee = employeeRepository.save(employee);

        Assertions.assertEquals(savedEmployee, employeeRepository.findById(savedEmployee.getUserName()).orElse(null));
        Assertions.assertNull(employeeRepository.findById("NonExistingID").orElse(null));
    }

}