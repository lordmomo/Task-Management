package com.momo.taskManagement.userFolder.repository;

import com.momo.taskManagement.userFolder.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task,Long> {

    Optional<Task> findByTaskName(String taskName);


//    @Query(nativeQuery = true,value = "")
//    List<Task> searchByTaskName(String taskname);

}
