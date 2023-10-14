package br.com.juliapereira.todolist.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.juliapereira.todolist.utils.Utils;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@Valid @RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null){
            return ResponseEntity.status(400).body("Username already in use");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);
        
        var userCreated = this.userRepository.save(userModel);

        return ResponseEntity.status(201).body(userCreated);
    }

    @GetMapping("/{userId}")
    public ResponseEntity retrieve(@PathVariable UUID userId){
        var user = this.userRepository.findById((UUID) userId).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        // Ainda falta fazer a verificação do usuário, pela autenticação.

        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity update(@Valid @PathVariable UUID userId, @RequestBody UserModel userModel){
        var user = this.userRepository.findById((UUID) userId).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        // Ainda falta fazer a verificação do usuário, pela autenticação.

        Utils.copyNonNullProperties(userModel, user);
        this.userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity update(@PathVariable UUID userId){
        var user = this.userRepository.findById((UUID) userId).orElse(null);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        // Ainda falta fazer a verificação do usuário, pela autenticação.

        this.userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }
}
