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
public class IndividualAlert extends Alert {

    private boolean read;
    private String userName;

    public IndividualAlert(int id, AlertType alertType, Topic topic, String message, Date expirationDate,
            boolean read, String userName) {
        super(id, alertType, topic, message, expirationDate);
        this.read = read;
        this.userName = userName;
    }

}
