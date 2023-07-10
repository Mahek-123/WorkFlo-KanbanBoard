package com.example.finalProject.repository;

import com.example.finalProject.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Employee findByUserNameAndPassword(String userName, String password);
}
