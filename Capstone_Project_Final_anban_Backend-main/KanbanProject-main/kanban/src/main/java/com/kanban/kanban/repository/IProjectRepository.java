package com.kanban.kanban.repository;

import com.kanban.kanban.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectRepository extends MongoRepository<Project, String> {
}
