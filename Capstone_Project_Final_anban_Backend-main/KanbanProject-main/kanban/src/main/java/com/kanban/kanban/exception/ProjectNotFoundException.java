package com.kanban.kanban.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND,reason = "Project does not Exist")
public class ProjectNotFoundException extends Exception {
}