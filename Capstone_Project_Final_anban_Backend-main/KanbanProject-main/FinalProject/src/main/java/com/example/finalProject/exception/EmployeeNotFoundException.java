package com.example.finalProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND, reason = "Employee with this is not found")
public class EmployeeNotFoundException extends Exception{
}