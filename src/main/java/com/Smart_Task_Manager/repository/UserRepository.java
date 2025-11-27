package com.Smart_Task_Manager.repository;

import com.Smart_Task_Manager.dto.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findById(long id);
}
