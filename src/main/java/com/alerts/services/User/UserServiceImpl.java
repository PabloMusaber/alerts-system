package com.alerts.services.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public UserServiceImpl(UserRepository userRepository, TopicRepository topicRepository) {
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

        User user = userRepository.getUser(userName);
        if (user == null) {
            throw new Exception("User " + userName + " doesn't exist.");
        }

        List<IndividualAlert> alerts = user.getAlerts();
        if (alerts.isEmpty()) {
            throw new Exception("No alerts available.");
        }

        Date currentDate = new Date();

        List<IndividualAlert> availableAlerts = alerts.stream()
                .filter(alert -> !alert.isRead() &&
                        (alert.getExpirationDate().after(currentDate) ||
                                alert.getExpirationDate().equals(currentDate)))
                .sorted(new AlertComparator())
                .collect(Collectors.toList());

        return availableAlerts;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    private Topic findTopicByName(String topicName) throws Exception {
        Topic topic = topicRepository.getTopics().stream()
                .filter(item -> item.getTopicName().equals(topicName))
                .findFirst()
                .orElseThrow(() -> new Exception("Topic doesn't exist."));

        return topic;
    }

}
