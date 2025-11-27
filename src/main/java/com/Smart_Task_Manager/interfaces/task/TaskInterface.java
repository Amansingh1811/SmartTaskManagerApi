package com.Smart_Task_Manager.interfaces.task;

import com.Smart_Task_Manager.dto.Tasks;
import org.springframework.scheduling.config.Task;

import java.util.List;

public interface TaskInterface {

    Tasks createTask(Tasks tasks);

    List<Tasks> getTasksByUserId(Long userId);

    Tasks updateTasks(Long id, Tasks tasks);

    void  deleteTask(Long id);

}
