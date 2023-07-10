package com.kanban.kanban.services;

import com.kanban.kanban.domain.NotificationDTO;
import com.kanban.kanban.domain.Project;
import com.kanban.kanban.domain.Task;
import com.kanban.kanban.exception.DuplicateProjectException;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.proxy.NotificationProxy;
import com.kanban.kanban.repository.IProjectRepository;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {
    private IProjectRepository projectRepository;
    private RabbitTemplate rabbitTemplate;
    private DirectExchange directExchange;
    private NotificationProxy notificationProxy;

    @Autowired
    public ProjectService(NotificationProxy notificationProxy,IProjectRepository projectRepository, RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.projectRepository = projectRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.notificationProxy= notificationProxy;
    }

    @Override
    public Project createProject(Project project) throws DuplicateProjectException {
        if (projectRepository.findById(project.getName()).isEmpty())
            return projectRepository.insert(project);
        throw new DuplicateProjectException();
    }

    @Override
    public Project getProject(String name) throws ProjectNotFoundException {
        if (projectRepository.findById(name).isEmpty())
            throw new ProjectNotFoundException();
        return projectRepository.findById(name).get();
    }

    @Override
    public boolean deleteProject(String name) throws ProjectNotFoundException {
        if (projectRepository.findById(name).isEmpty())
            throw new ProjectNotFoundException();
        projectRepository.deleteById(name);
        return true;
    }

    @Override
    public boolean saveChanges(String name, Map<String, List<Task>> columns) throws ProjectNotFoundException {
        if (projectRepository.findById(name).isEmpty())
            throw new ProjectNotFoundException();
        Project project = projectRepository.findById(name).get();
        project.setColumns(columns);
        projectRepository.save(project);
        return true;
    }

    @Override
    public Project addNewTask(String name, Task task) {

        Project project = projectRepository.findById(name).get();
//        boolean flag = project.getColumns().get("To Be Done")
//                .stream().anyMatch(t -> t.getName().equals(task.getName()));

        boolean flag=false;
        Map<String, List<Task>> columns=projectRepository.findById(name).get().getColumns();

        for(String key: columns.keySet()){
            List<Task> tasks =columns.get(key);
            for(Task eachTask:tasks){
                if(eachTask.getName().equals(task.getName())){
                    flag=true;
                }
            }
        }
        if (flag) {
            throw new IllegalArgumentException("Task with the same name already exists");
        }
        for (String obj : task.getMembers()) {
            String message = "Added to task : " + task.getName();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Notification", message);
            jsonObject.put("username", obj);

            NotificationDTO notificationDTO = new NotificationDTO(jsonObject);
            rabbitTemplate.convertAndSend(directExchange.getName(), "user-routing", notificationDTO);
        }
        project.getColumns().get("To Be Done").add(task);
        return projectRepository.save(project);
    }

    @Override
    public boolean deleteMemeberFromProject(String projectName, String userName) throws ProjectNotFoundException {
        Optional<Project> optionalProject = projectRepository.findById(projectName);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            List<String> listMembers = project.getMembers();
            if (listMembers.contains(userName)) {
                String message = "Project Deleted: " + userName;
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Notification", message);
                jsonObject.put("username", userName);
                NotificationDTO notificationDTO = new NotificationDTO(jsonObject);
                rabbitTemplate.convertAndSend(directExchange.getName(), "user-routing", notificationDTO);

                listMembers.removeIf(member -> member.equals(userName));
                if (listMembers.isEmpty()) {
                    deleteProject(projectName);
                } else {
                    for (String member : listMembers) {
                        String message1 = "Project Member Removed: " + userName;
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("Notification", message1);
                        jsonObject1.put("username", member);
                        NotificationDTO notificationDTO1 = new NotificationDTO(jsonObject1);
                        rabbitTemplate.convertAndSend(directExchange.getName(), "user-routing", notificationDTO1);
                    }
                    project.setMembers(listMembers);
                    projectRepository.save(project);
                }
                return true;
            }
        }
        throw new ProjectNotFoundException();
    }

    @Override
    public Project editProject(String name, Project project1) throws ProjectNotFoundException {
        if(projectRepository.findById(name).isEmpty()) {
            throw new ProjectNotFoundException();
        }
        Project project= projectRepository.findById(name).get();
        project.setMembers(project1.getMembers());
        return projectRepository.save(project);
    }

}

