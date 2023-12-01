package com.alerts.entities;

import java.util.Date;

import com.alerts.enums.AlertType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Alert {
    private int id;
    private AlertType alertType;
    private Topic topic;
    private String message;
    private Date expirationDate;
}
