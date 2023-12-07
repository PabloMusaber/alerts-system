package com.alerts.DTO;

import java.util.Date;

import lombok.Getter;

@Getter
public class IndividualAlertDTO {
    private String type;
    private String topicName;
    private String message;
    private Date expirationDate;
    private String userName;
}
