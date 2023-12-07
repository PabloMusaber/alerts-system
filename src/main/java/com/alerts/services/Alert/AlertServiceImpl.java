package com.alerts.services.Alert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    private final UserRepository userRepository;

    private final TopicRepository topicRepository;

    private final AlertRepository alertRepository;

    public AlertServiceImpl(UserRepository userRepository,
            TopicRepository topicRepository, AlertRepository alertRepository) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public Alert globalAlert(AlertDTO alertDto) throws Exception {

        Alert alert = new Alert();

        try {
            alert.setAlertType(parseAlertType(alertDto.getType()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error parsing alert type.", e);
        }

        List<Topic> topics = topicRepository.getTopics();
        Topic selectedTopic = topics.stream()
                .filter(topic -> topic.getTopicName().equals(alertDto.getTopicName()))
                .findFirst()
                .orElseThrow(() -> new Exception("The selected topic does not exist."));
        alert.setTopic(selectedTopic);

        alert.setMessage(alertDto.getMessage());
        alert.setExpirationDate(alertDto.getExpirationDate());

        HashMap<String, User> users = userRepository.getUsersMap();

        for (User user : users.values()) {
            if (user.getSuscribedTopics().contains(alert.getTopic())) {
                IndividualAlert individualAlert = new IndividualAlert();
                individualAlert.setAlertType(alert.getAlertType());
                individualAlert.setTopic(alert.getTopic());
                individualAlert.setMessage(alert.getMessage());
                individualAlert.setExpirationDate(alert.getExpirationDate());
                individualAlert.setRead(false);
                individualAlert.setUserName(user.getUserName());
                individualAlert.setId(user.getAlerts().size());
                user.getAlerts().add(individualAlert);
            }
        }

        userRepository.saveUsers(users);
        alert.setId(alertRepository.getAlerts().size());
        alertRepository.addAlert(alert);
        return alert;
    }

    @Override
    public IndividualAlert individualAlert(IndividualAlertDTO individualAlertDto) throws Exception {
        IndividualAlert individualAlert = new IndividualAlert();

        try {
            individualAlert.setAlertType(parseAlertType(individualAlertDto.getType()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error parsing alert type.", e);
        }

        List<Topic> topics = topicRepository.getTopics();
        Topic selectedTopic = topics.stream()
                .filter(topic -> topic.getTopicName().equals(individualAlertDto.getTopicName()))
                .findFirst()
                .orElseThrow(() -> new Exception("The selected topic does not exist."));
        individualAlert.setTopic(selectedTopic);

        individualAlert.setMessage(individualAlertDto.getMessage());
        individualAlert.setExpirationDate(individualAlertDto.getExpirationDate());
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
        User user = userRepository.getUser(markAsReadDto.getUserName());
        if (user == null) {
            throw new Exception("User " + markAsReadDto.getUserName() + " doesn't exist.");
        }

        List<IndividualAlert> alerts = user.getAlerts();

        if (alerts == null || markAsReadDto.getAlertNumber() < 0 || markAsReadDto.getAlertNumber() >= alerts.size()) {
            throw new Exception("Alert not found.");
        }

        alerts.get(markAsReadDto.getAlertNumber()).setRead(true);
        return alerts.get(markAsReadDto.getAlertNumber());
    }

    @Override
    public List<Alert> getAlertsByTopic(Topic topic) throws Exception {
        List<Alert> alerts = alertRepository.getAlerts();
        if (alerts == null) {
            throw new Exception("Error al obtener alertas.");
        }

        Date currentDate = new Date();

        List<Alert> currentAlerts = alerts.stream()
                .filter(alert -> alert.getTopic().getTopicName().equals(topic.getTopicName())
                        && (alert.getExpirationDate().after(currentDate)
                                || alert.getExpirationDate().equals(currentDate)))
                .sorted(new AlertComparator())
                .collect(Collectors.toList());

        return currentAlerts;
    }

    private AlertType parseAlertType(String type) {
        try {
            if (type != null) {
                return AlertType.valueOf(type.toUpperCase());
            } else {
                throw new IllegalArgumentException("The alert type cannot be null.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid alert type.", e);
        }
    }

}