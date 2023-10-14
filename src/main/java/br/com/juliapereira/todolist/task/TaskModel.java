package br.com.juliapereira.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import br.com.juliapereira.todolist.user.UserModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity()
@Table(name="tb_tasks")
@Data
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    
    
    @Column(length = 50)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    
    private String priority;

    @ManyToOne
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private UserModel user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception{

        if(title.length() > 50){
            throw new Exception("O campo title deve conter no máximo 50 caracteres.");
        }
        this.title = title;
    }

}
