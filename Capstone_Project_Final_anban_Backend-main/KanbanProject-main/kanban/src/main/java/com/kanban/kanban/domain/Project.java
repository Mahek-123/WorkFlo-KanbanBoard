package com.kanban.kanban.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Project {
    @Id
    private String name;
    private List<String> members;
    private Map<String,List<Task>> columns;

}
