package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.TaskRepository;
import com.application.taskmanagementsystem.dao.UserRepository;
import com.application.taskmanagementsystem.model.dto.TaskRequest;
import com.application.taskmanagementsystem.model.entity.Task;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.security.JwtCore;
import com.application.taskmanagementsystem.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@Transactional
@CacheConfig(cacheNames = "dataUser")
public class UserService implements UserDetailsService {
    private JwtCore jwtCore;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private static final String USER_NOT_FOUND_MESSAGE = "User with name \"%s\" already exists";
    private static final String TASK_CREATED="Task \"%s\"  has been created successfully";
    @Autowired
    public UserService(
            UserRepository userRepository,TaskRepository taskRepository,JwtCore jwtCore) {
        this.userRepository=userRepository;
        this.taskRepository=taskRepository;
        this.jwtCore=jwtCore;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findUserByUsername(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
        return UserDetailsImpl.build(user);
    }
    public String createTask(TaskRequest taskRequest, HttpServletRequest request){
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDefinition());
        task.setPriority(taskRequest.getPriority());
        task.setUser(userRepository.findUserByUsername(jwtCore.getNameFromJwt(request)).get());
        taskRepository.save(task);
        return String.format(TASK_CREATED,task.getTitle());
    }
}
