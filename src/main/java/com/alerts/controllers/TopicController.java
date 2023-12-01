package com.alerts.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alerts.entities.Topic;
import com.alerts.services.Topic.TopicService;

@RestController
@RequestMapping("/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping("")
    public ResponseEntity<?> crearTema(@RequestBody Topic topic) {
        return ResponseEntity.ok(topicService.createTopic(topic));
    }

    @GetMapping("")
    public List<Topic> obtenerTemas() {
        return topicService.getTopics();
    }
}
