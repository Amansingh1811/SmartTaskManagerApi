package com.Smart_Task_Manager.service;

import com.Smart_Task_Manager.dto.Tasks;
import com.Smart_Task_Manager.dto.Users;
import com.Smart_Task_Manager.exception.ResourceNotFoundException;
import com.Smart_Task_Manager.interfaces.user.UserInterface;
import com.Smart_Task_Manager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserInterface {

    Logger detailedLogger = LoggerFactory.getLogger("detailedLogger");
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users createUser(Users user) {

        Users users = new Users();

        users.setPassword(passwordEncoder.encode(user.getPassword()));
        detailedLogger.info("Encoded password for user:" , passwordEncoder.encode(user.getPassword()));
        users.setEmail(user.getEmail());
        users.setName(user.getName());
        users.setRole(user.getRole());

        if (user.getTasks() != null){
            detailedLogger.info("Associating tasks with user: " + user.getName());
            for (Tasks task : user.getTasks()) {
                task.setUsers(users);
            }
            users.setTasks(user.getTasks());
        }

        return userRepository.save(users);
    }

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users updateUser(Long id, Users user) {
        Users updateUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(user.getPassword());
        updateUser.setRole(user.getRole());

        if (user.getTasks() != null){
            for (Tasks task : user.getTasks()) {
                task.setUsers(updateUser);
            }
        }
        userRepository.save(updateUser);
        return updateUser;
    }

    @Override
    public void deleteUser(Long id) {
        Users user = getUserById(id);
        userRepository.delete(user);
    }
}
