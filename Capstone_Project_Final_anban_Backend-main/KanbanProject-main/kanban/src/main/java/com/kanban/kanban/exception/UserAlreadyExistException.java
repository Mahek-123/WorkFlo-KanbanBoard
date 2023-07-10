package com.kanban.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT ,reason = "User with this name already exist")
public class UserAlreadyExistException extends Exception{
}