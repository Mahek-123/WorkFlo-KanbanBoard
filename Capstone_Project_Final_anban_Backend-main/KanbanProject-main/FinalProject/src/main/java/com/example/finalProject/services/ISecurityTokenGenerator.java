package com.example.finalProject.services;

import com.example.finalProject.domain.Employee;

import java.util.Map;

public interface ISecurityTokenGenerator {
    Map<String, String> generateToken(Employee employee);
}
