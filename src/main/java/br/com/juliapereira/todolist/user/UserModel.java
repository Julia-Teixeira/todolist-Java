package br.com.juliapereira.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="tb_users")
@Data
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(length=92, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String password;  
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setName(String name) throws Exception{

        if(name.length() > 92){
            throw new Exception("O campo name deve conter no máximo 92 caracteres.");
        }
        this.name = name;
    }
}
