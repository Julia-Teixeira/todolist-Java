package br.com.juliapereira.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import br.com.juliapereira.todolist.user.UserModel;



public interface ITaskRepository extends JpaRepository<TaskModel, UUID>{
    List<TaskModel> findByUser(UserModel user);
}
