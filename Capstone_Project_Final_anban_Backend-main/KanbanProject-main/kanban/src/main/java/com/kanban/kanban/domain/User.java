package com.kanban.kanban.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class User {
    @Id
    private String name;
    private String email;
    private String password;
    private String phone;
    private List<String> projectList;
}
