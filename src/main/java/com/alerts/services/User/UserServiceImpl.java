package com.alerts.services.User;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alerts.DTO.SubscribedTopicDTO;
import com.alerts.entities.IndividualAlert;
import com.alerts.entities.Topic;
import com.alerts.entities.User;
import com.alerts.repositories.TopicRepository;
import com.alerts.repositories.UserRepository;
import com.alerts.services.Alert.AlertComparator;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private TopicRepository topicRepository;

    @Override
    public User createUser(String userName) {
        User newUser = new User();
        newUser.setUserName(userName);
        userRepository.addUser(newUser);
        return newUser;
    }

    @Override
    public User subscribeTopic(SubscribedTopicDTO subscribedTopicDto) throws Exception {
        List<Topic> topics = topicRepository.getTopics();
        User user = userRepository.getUser(subscribedTopicDto.getUserName());

        if (user == null) {
            throw new Exception("User doesn't exist.");
        }

        for (Topic item : topics) {
            if (item.getTopicName().equals(subscribedTopicDto.getTopicName())) {
                // Encuentro el tema y lo agrego en la lista de subscriptos
                user.subscribeTopic(item);
                return user;
            }
        }
        throw new Exception("El tema no existe.");
    }

    @Override
    public List<IndividualAlert> getAlertsByUser(String userName) throws Exception {
        try {
            User user = userRepository.getUser(userName);
            List<IndividualAlert> alerts = user.getAlerts();
            List<IndividualAlert> availableAlerts = new ArrayList<>();
            Date currentDate = new Date();

            for (IndividualAlert alert : alerts) {
                // Busco las alerts NO experidas y NO leídas
                if ((alert.isRead() == false)
                        && (alert.getExpirationDate().after(currentDate)
                                || alert.getExpirationDate().equals(currentDate))) {
                    availableAlerts.add(alert);
                }
            }

            Collections.sort(availableAlerts, new AlertComparator());

            return availableAlerts;

        } catch (Exception e) {
            throw new Exception("No alerts available.");
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

}
