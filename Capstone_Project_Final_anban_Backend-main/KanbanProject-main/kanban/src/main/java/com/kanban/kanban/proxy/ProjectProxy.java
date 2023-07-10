package com.kanban.kanban.proxy;

import com.kanban.kanban.exception.ProjectNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "kanbanMainService", url = "http://localhost:8007")
public interface ProjectProxy {

    @GetMapping("/api/v1/project/deleteMember/{projectName}/{userName}")
    ResponseEntity<?> deleteMemberOfProject(@PathVariable String projectName, @PathVariable String userName) throws ProjectNotFoundException;
}

