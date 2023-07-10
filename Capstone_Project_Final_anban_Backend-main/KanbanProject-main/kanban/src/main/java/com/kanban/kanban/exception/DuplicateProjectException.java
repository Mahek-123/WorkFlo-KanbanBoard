package com.kanban.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.CONFLICT,reason = "Project already exists")
public class DuplicateProjectException extends Exception{
}
