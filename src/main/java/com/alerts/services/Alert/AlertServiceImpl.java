package com.alerts.services.Alert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alerts.DTO.AlertDTO;
import com.alerts.DTO.IndividualAlertDTO;
import com.alerts.DTO.MarkAsReadDTO;
import com.alerts.entities.Alert;
import com.alerts.entities.IndividualAlert;
import com.alerts.entities.Topic;
import com.alerts.entities.User;
import com.alerts.enums.AlertType;
import com.alerts.repositories.AlertRepository;
import com.alerts.repositories.TopicRepository;
import com.alerts.repositories.UserRepository;

@Service
public class AlertServiceImpl implements AlertService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private AlertRepository alertRepository;

    @Override
    public Alert globalAlert(AlertDTO alertDto) throws Exception {

        // Crea una nueva instancia de Alert para cada alert global
        Alert alert = new Alert();

        // Chequeo que el tipo sea válido
        if (alertDto.getType().equals("Informativa")) {
            alert.setAlertType(AlertType.INFORMATIVE);
        } else if (alertDto.getType().equals("Urgente")) {
            alert.setAlertType(AlertType.URGENT);
        } else {
            throw new Exception("The selected alert type does not exist.");
        }

        // Chequeo que el topic sea válido
        List<Topic> topics = topicRepository.getTopics();
        for (Topic topic : topics) {
            if (topic.getTopicName().equals(alertDto.getTopicName())) {
                alert.setTopic(topic);
                break;
            }
        }

        if (alert.getTopic() == null) {
            throw new Exception("The selected topic does not exist.");
        }

        alert.setMessage(alertDto.getMessage());
        alert.setExpirationDate(alertDto.getExpirationDate());

        HashMap<String, User> users = userRepository.getUsersMap();

        for (User user : users.values()) {
            // Crear una nueva instancia de Alert Individual para cada user, para poder
            // guardar su estado de leída
            IndividualAlert individualAlert = new IndividualAlert();
            individualAlert.setAlertType(alert.getAlertType());
            individualAlert.setTopic(alert.getTopic());
            individualAlert.setMessage(alert.getMessage());
            individualAlert.setExpirationDate(alert.getExpirationDate());
            individualAlert.setRead(false);
            individualAlert.setUserName(user.getUserName());
            // El id de cada alert será distinto para cada user, en base a la posición
            // que ocupe la alert dentro de su la lista de alertas
            individualAlert.setId(user.getAlerts().size());

            if (user.getSuscribedTopics().contains(individualAlert.getTopic())) {
                user.getAlerts().add(individualAlert);
            }
        }

        userRepository.saveUsers(users);

        // El id que se guarda en la lista global de alertas corresponde al número total
        // de alertas emitidas, contando globales como individuales.
        alert.setId(alertRepository.getAlerts().size());
        alertRepository.addAlert(alert);
        return alert;
    }

    @Override
    public IndividualAlert individualAlert(IndividualAlertDTO individualAlertDto) throws Exception {
        IndividualAlert individualAlert = new IndividualAlert();

        if (individualAlertDto.getType().equals("Informativa")) {
            individualAlert.setAlertType(AlertType.INFORMATIVE);
        } else if (individualAlertDto.getType().equals("Urgente")) {
            individualAlert.setAlertType(AlertType.URGENT);
        } else {
            throw new Exception("The selected alert type does not exist.");
        }

        List<Topic> topics = topicRepository.getTopics();
        for (Topic topic : topics) {
            if (topic.getTopicName().equals(individualAlertDto.getTopicName())) {
                individualAlert.setTopic(topic);
                break;
            }
        }

        if (individualAlert.getTopic() == null) {
            throw new Exception("The selected topic does not exist.");
        }

        individualAlert.setMessage(individualAlertDto.getMessage());
        individualAlert.setExpirationDate(individualAlertDto.getExperationDate());
        individualAlert.setUserName(individualAlertDto.getUserName());
        individualAlert.setRead(false);

        User user = userRepository.getUser(individualAlertDto.getUserName());
        if (user.getSuscribedTopics().contains(individualAlert.getTopic())) {
            individualAlert.setId(user.getAlerts().size());
            user.getAlerts().add(individualAlert);
            alertRepository.addAlert(individualAlert);
            return individualAlert;
        } else {
            throw new Exception(
                    "El user " + individualAlertDto.getUserName() + " no está subscripto al topic "
                            + individualAlertDto.getTopicName());
        }
    }

    @Override
    public IndividualAlert markAsRead(MarkAsReadDTO markAsReadDto) throws Exception {
        try {
            User user = userRepository.getUser(markAsReadDto.getUserName());
            user.getAlerts().get(markAsReadDto.getAlertNumber()).setRead(true);
            return user.getAlerts().get(markAsReadDto.getAlertNumber());
        } catch (Exception e) {
            throw new Exception("Doesn't exist alert number " + markAsReadDto.getAlertNumber() + " for "
                    + markAsReadDto.getUserName());
        }
    }

    @Override
    public List<Alert> getAlertsByTopic(Topic topic) throws Exception {
        try {
            List<Alert> alerts = alertRepository.getAlerts();
            List<Alert> currentAlerts = new ArrayList<>();
            Date currentDate = new Date();

            for (Alert alert : alerts) {
                // Verifico que la alert corresponda al topic y que NO esté expirada
                if ((alert.getTopic().getTopicName().equals(topic.getTopicName()))
                        && (alert.getExpirationDate().after(currentDate)
                                || alert.getExpirationDate().equals(currentDate))) {
                    currentAlerts.add(alert);
                }
            }

            // Realizo el ordenamiento requerido
            Collections.sort(currentAlerts, new AlertComparator());
            return currentAlerts;
        } catch (Exception e) {
            throw new Exception("Error al obtener alertas.");
        }
    }

}
