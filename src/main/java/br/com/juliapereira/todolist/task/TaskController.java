package br.com.juliapereira.todolist.task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.juliapereira.todolist.user.UserModel;
import br.com.juliapereira.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
    
    @PostMapping("/")   
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
        UserModel user = (UserModel) request.getAttribute("user");
        taskModel.setUser(user);

        var currentDate = LocalDateTime.now();
        
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.badRequest().body("error: A data de início/término tem que ser maior que a data atual.");
        }

                
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.badRequest().body("error: A data de início deve ser menor que a data de término.");
        }

        var createdTask = this.taskRepository.save(taskModel);

        return ResponseEntity.status(201).body(createdTask);
    }

    @GetMapping("/") 
    public ResponseEntity list(HttpServletRequest request){
        UserModel user = (UserModel) request.getAttribute("user");
        var allTasks =  this.taskRepository.findByUser(user);
        return ResponseEntity.ok().body(allTasks);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID taskId, HttpServletRequest request){

        var task = this.taskRepository.findById(taskId).orElse(null);

        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada!");
        }

        if(!task.getUser().equals(request.getAttribute("user"))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário sem permissão para alterar essa tarefa!");
        }
        
        Utils.copyNonNullProperties(taskModel, task);
        this.taskRepository.save(task);
        return ResponseEntity.ok().body(task);
     
       
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity delete(@PathVariable UUID taskId, HttpServletRequest request){
        var task = this.taskRepository.findById(taskId).orElse(null);

        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada!");
        }

        if(!task.getUser().equals(request.getAttribute("user"))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário sem permissão para deletar essa tarefa!");
        }

        this.taskRepository.delete(task);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
