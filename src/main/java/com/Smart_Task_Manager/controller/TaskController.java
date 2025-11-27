package com.Smart_Task_Manager.controller;

import com.Smart_Task_Manager.dto.Tasks;
import com.Smart_Task_Manager.dto.Users;
import com.Smart_Task_Manager.repository.UserRepository;
import com.Smart_Task_Manager.service.TaskService;
import com.Smart_Task_Manager.service.UserService;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/user")
public class TaskController {

    private final UserService userService;
    private final UserRepository userRepository;
    Logger detailedLogger = LoggerFactory.getLogger("detailedLogger");
    private final TaskService taskService;

    public TaskController(TaskService taskService, UserService userService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("create/tasks")
    public ResponseEntity<Tasks> creteTask(@RequestBody Tasks tasks, Principal principal) {
        detailedLogger.info("Creating a new task with title: {}", tasks.getTitle());
        detailedLogger.info("principal : {}", principal.getName());
        String email = principal.getName();
        Users user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        detailedLogger.info("User found: {}", user);
        tasks.setUsers(user);
        Tasks newTask = taskService.createTask(tasks);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<Tasks>> getUserById(@PathVariable Long id) {
        detailedLogger.info("Fetching tasks for user id: {}", id);
        List<Tasks> tasks = taskService.getTasksByUserId(id);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping("/{id}")
    public Tasks updateTask(@PathVariable Long id, @RequestBody Tasks user) {
        return taskService.updateTasks(id, user);
    }

    @DeleteMapping("/{id}")
    public String deleteTasks(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "User deleted successfully!";
    }
}
