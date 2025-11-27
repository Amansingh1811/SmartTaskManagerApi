package com.Smart_Task_Manager.service;

import com.Smart_Task_Manager.dto.Tasks;
import com.Smart_Task_Manager.dto.Users;
import com.Smart_Task_Manager.exception.ResourceNotFoundException;
import com.Smart_Task_Manager.interfaces.task.TaskInterface;
import com.Smart_Task_Manager.repository.TaskRepository;
import com.Smart_Task_Manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
public class TaskService implements TaskInterface {
    Logger detailedLogger = LoggerFactory.getLogger("detailedLogger");

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Tasks createTask(Tasks tasks) {
        detailedLogger.info("Creating a new task with title: {}", tasks);
        if (tasks.getUsers() != null){
            detailedLogger.info("Associating task with user id: {}", tasks.getUsers().getId());
            Users users = userService.getUserById(tasks.getUsers().getId());
            detailedLogger.info("users", users);
            if (users.getTasks() == null) {
                users.setTasks(new ArrayList<>());
            }

            users.getTasks().add(tasks);
        }
        return taskRepository.save(tasks);
    }

    @Override
    public List<Tasks> getTasksByUserId(Long userId) {
        List<Tasks> tasks = taskRepository.findByUserId(userId);
        return tasks;
    }

    @Override
    public Tasks updateTasks(Long id, Tasks updatedTasks) {
        Tasks task = taskRepository.findTasksById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setTitle(updatedTasks.getTitle());
        task.setDescription(updatedTasks.getDescription());
        task.setComplete(updatedTasks.isComplete());
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        Tasks task = taskRepository.findTasksById(id).orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskRepository.delete(task);
    }
}
