package com.kanban.kanban.services;

import com.kanban.kanban.domain.NotificationDTO;
import com.kanban.kanban.domain.EmployeeDTO;
import com.kanban.kanban.domain.User;
import com.kanban.kanban.exception.ProjectNotFoundException;
import com.kanban.kanban.exception.UserAlreadyExistException;
import com.kanban.kanban.exception.UserNotFoundException;
import com.kanban.kanban.proxy.NotificationProxy;
import com.kanban.kanban.proxy.ProjectProxy;
import com.kanban.kanban.proxy.UserProxy;
import com.kanban.kanban.repository.IUserRepository;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;
    private UserProxy userProxy;
    private RabbitTemplate rabbitTemplate;
    private DirectExchange directExchange;
    private ProjectProxy projectProxy;
    private NotificationProxy notificationProxy;

    @Autowired
    public UserService(IUserRepository userRepository, UserProxy userProxy, RabbitTemplate rabbitTemplate, DirectExchange directExchange, ProjectProxy projectProxy, NotificationProxy notificationProxy) {
        this.userRepository = userRepository;
        this.userProxy = userProxy;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.projectProxy = projectProxy;
        this.notificationProxy = notificationProxy;
    }

    @Override
    public User registerUser(User user) throws UserAlreadyExistException {

        String emailBody = "Welcome aboard to WorkFlo! We're thrilled to have you join our growing community of productivity enthusiasts." +
                " As the creator of a dedicated platform for Kanban board management, we are committed to helping you streamline your workflow, boost collaboration, and accomplish your goals with ease.\n\n" +
                "At WorkFlo, we understand the importance of effective project management, and our intuitive Kanban board system is designed to simplify " +
                "the process for you. Whether you're an individual looking to organize personal tasks or a team striving for seamless teamwork, our platform offers the ideal solution.\n\n" +
                "Once logged in, you'll be greeted by an intuitive interface where you can create boards, add and manage tasks, collaborate with team members, and monitor progress effortlessly." +
                " Our platform is designed to adapt to your unique workflow, allowing you to customize columns, labels, and other features to align with your specific needs.\n\n" +
                "Moreover, we constantly strive to enhance your experience with regular updates and new features. Stay tuned for upcoming enhancements that will further empower you to stay organized," +
                " increase productivity, and achieve your objectives effectively.\n\n" +
                "Should you encounter any questions or require assistance, our support team is always ready to help. Feel free to reach out to us at workflo.site@gmail.com, and we'll respond promptly to address your queries.\n\n" +
                "Thank you for choosing WorkFlo as your go-to platform for Kanban board management. We're excited to have you join us on this productivity journey!\n\n" +
                "Best regards,\n" +
                "WorkFlo Team";

        if (userRepository.findById(user.getName()).isEmpty()) {
            EmployeeDTO employeeDTO = new EmployeeDTO(user.getName(), user.getPassword());
            System.out.println(user);
            notificationProxy.sendRegistrationEmail(user.getEmail(),emailBody);
            userProxy.addNewUser(employeeDTO);

            String message = "Welcome " + user.getName();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Notification", message);
            jsonObject.put("username", user.getName());
            NotificationDTO notificationDTO = new NotificationDTO(jsonObject);
            rabbitTemplate.convertAndSend(directExchange.getName(), "user-routing", notificationDTO);
            return userRepository.insert(user);
        } else {
            throw new UserAlreadyExistException();
        }
    }

    @Override
    public User userDetails(String userName) throws UserNotFoundException {
        if (userRepository.findById(userName).isEmpty()) {
            throw new UserNotFoundException();
        } else {
            return userRepository.findById(userName).get();
        }
    }

    @Override
    public boolean addProjectList(String userName, String projectName) throws UserNotFoundException, ProjectNotFoundException {
        if (userRepository.findById(userName).isEmpty()) {
            throw new UserNotFoundException();
        } else {
            User user = userRepository.findById(userName).get();
            List<String> list = user.getProjectList();
            if(!list.contains(projectName)){
                list.add(projectName);
                user.setProjectList(list);
                String message = "You were added to the project: " + projectName;
                String emailBody="You were added the project "+projectName;
                notificationProxy.sendRegistrationEmail(user.getEmail(),emailBody);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Notification", message);
                jsonObject.put("username", userName);
                NotificationDTO notificationDTO = new NotificationDTO(jsonObject);
                rabbitTemplate.convertAndSend(directExchange.getName(), "user-routing", notificationDTO);
                userRepository.save(user);
                return true;
            }

            return false;
        }
    }

    @Override
    public boolean removeProjectList(String userName, String projectName) throws UserNotFoundException, ProjectNotFoundException {
        if (userRepository.findById(userName).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user_ = userRepository.findById(userName).get();
        List<String> projectList = user_.getProjectList();
        if (!projectList.contains(projectName)) {
            throw new ProjectNotFoundException();
        } else {
            projectProxy.deleteMemberOfProject(projectName, userName);
            List<String> list = user_.getProjectList();
            list.remove(projectName);
            user_.setProjectList(list);
            String message = "You were removed from Project: " + projectName;
            String emailBody="You were removed from the project "+projectName;
            notificationProxy.sendRegistrationEmail(user_.getEmail(),emailBody);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Notification", message);
            jsonObject.put("username", userName);
            NotificationDTO notificationDTO = new NotificationDTO(jsonObject);
            rabbitTemplate.convertAndSend(directExchange.getName(), "user-routing", notificationDTO);
            userRepository.save(user_);
            return true;
        }
    }

    @Override
    public List<String> getProjectList(String userName) throws UserNotFoundException {
        if (userRepository.findById(userName).isEmpty()) {
            throw new UserNotFoundException();
        } else {
            return userRepository.findById(userName).get().getProjectList();
        }
    }
}