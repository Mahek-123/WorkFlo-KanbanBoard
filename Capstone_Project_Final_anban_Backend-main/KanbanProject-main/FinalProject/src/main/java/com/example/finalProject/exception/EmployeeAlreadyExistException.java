package com.example.finalProject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT, reason = "Employee with this id already exist")
public class EmployeeAlreadyExistException extends Exception{

}