package com.alerts.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.alerts.entities.Alert;

import lombok.Getter;

@Getter
@Repository
public class AlertRepository {
    private List<Alert> alerts = new ArrayList<>();

    public void addAlert(Alert alert) {
        alerts.add(alert);
    }
}
