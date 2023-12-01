package com.alerts.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alerts.DTO.MessageDTO;
import com.alerts.DTO.SubscribedTopicDTO;
import com.alerts.DTO.UserDTO;
import com.alerts.entities.User;
import com.alerts.services.User.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
        return ResponseEntity.ok(userService.createUser(user.getUserName()));
    }

    @GetMapping("")
    public List<User> geUserst() {
        return userService.getUsers();
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeTopic(@RequestBody SubscribedTopicDTO subscribeTopicDto) {
        try {
            return ResponseEntity.ok(userService.subscribeTopic(subscribeTopicDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<?> obtenerAlertas(@RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok(userService.getAlertsByUser(user.getUserName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
        }
    }

}
