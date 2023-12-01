package com.alerts.services.Alert;

import java.util.List;

import com.alerts.DTO.AlertDTO;
import com.alerts.DTO.IndividualAlertDTO;
import com.alerts.DTO.MarkAsReadDTO;
import com.alerts.entities.Alert;
import com.alerts.entities.IndividualAlert;
import com.alerts.entities.Topic;

public interface AlertService {

    public Alert globalAlert(AlertDTO alertDto) throws Exception;

    public IndividualAlert individualAlert(IndividualAlertDTO individualAlertDto) throws Exception;

    public IndividualAlert markAsRead(MarkAsReadDTO markAsReadDto) throws Exception;

    public List<Alert> getAlertsByTopic(Topic topic) throws Exception;

}
