package com.Smart_Task_Manager.interfaces.user;

import com.Smart_Task_Manager.dto.Users;

import java.util.List;

public interface UserInterface {

    Users createUser(Users user);
    Users getUserById(Long id);
    List<Users> getAllUsers();
    Users updateUser(Long id, Users user);
    void deleteUser(Long id);

}
