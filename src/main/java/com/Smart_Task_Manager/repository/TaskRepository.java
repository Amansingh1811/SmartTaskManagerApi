package com.Smart_Task_Manager.repository;

import com.Smart_Task_Manager.dto.Tasks;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long> {

    @Query("SELECT t FROM Tasks t WHERE t.users.id = :userId")
    List<Tasks> findByUserId(@Param("userId") Long userId);

    Optional<Tasks> findTasksById(Long id);


}
