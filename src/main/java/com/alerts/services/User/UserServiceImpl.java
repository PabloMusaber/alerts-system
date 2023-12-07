package com.alerts.services.User;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alerts.DTO.SubscribedTopicDTO;
import com.alerts.entities.IndividualAlert;
import com.alerts.entities.Topic;
import com.alerts.entities.User;
import com.alerts.repositories.TopicRepository;
import com.alerts.repositories.UserRepository;
import com.alerts.services.Alert.AlertComparator;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TopicRepository topicRepository;

    public UserServiceImpl (UserRepository userRepository, TopicRepository topicRepository){
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
    }

    @Override
    public User createUser(String userName) {
        User newUser = new User();
        newUser.setUserName(userName);
        userRepository.addUser(newUser);
        return newUser;
    }

    @Override
    public User subscribeTopic(SubscribedTopicDTO subscribedTopicDto) throws Exception {
        User user = userRepository.getUser(subscribedTopicDto.getUserName());

        if (user == null) {
            throw new Exception("User doesn't exist.");
        }

        Topic topic = findTopicByName(subscribedTopicDto.getTopicName());
        user.subscribeTopic(topic);
        return user;
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

    private Topic findTopicByName(String topicName) throws Exception {
        List<Topic> topics = topicRepository.getTopics();

        for (Topic item : topics) {
            if (item.getTopicName().equals(topicName)) {
                return item;
            }
        }

        throw new Exception("Topic doesn't exist.");
    }

}
