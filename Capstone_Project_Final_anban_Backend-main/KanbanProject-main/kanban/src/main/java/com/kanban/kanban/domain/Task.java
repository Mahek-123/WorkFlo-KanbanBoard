package com.kanban.kanban.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    private String name;
    private String content;
    private String priority;
    private String createDate;
    private String deadline;
    private String assignee;
    private String status;
    private List<String> members;
}
