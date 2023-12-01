package com.alerts.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alerts.DTO.AlertDTO;
import com.alerts.DTO.IndividualAlertDTO;
import com.alerts.DTO.MarkAsReadDTO;
import com.alerts.DTO.MessageDTO;
import com.alerts.entities.Topic;
import com.alerts.services.Alert.AlertService;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping("/global")
    public ResponseEntity<?> globalAlert(@RequestBody AlertDTO alertDto) {
        try {
            return ResponseEntity.ok(alertService.globalAlert(alertDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
        }

    }

    @PostMapping("/individual")
    public ResponseEntity<?> individualAlert(@RequestBody IndividualAlertDTO individualAlertDto) {
        try {
            return ResponseEntity.ok(alertService.individualAlert(individualAlertDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
        }
    }

    @PatchMapping("/leida")
    public ResponseEntity<?> marcarLeida(@RequestBody MarkAsReadDTO markAsReadDto) {
        try {
            return ResponseEntity.ok(alertService.markAsRead(markAsReadDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
        }
    }

    @GetMapping("/tema")
    public ResponseEntity<?> obtenerAlertasPorTema(@RequestBody Topic topic) {
        try {
            return ResponseEntity.ok(alertService.getAlertsByTopic(topic));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
        }
    }

}
