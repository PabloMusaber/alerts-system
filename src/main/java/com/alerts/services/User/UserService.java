package com.alerts.services.User;

import java.util.List;

import com.alerts.DTO.SubscribedTopicDTO;
import com.alerts.entities.IndividualAlert;
import com.alerts.entities.User;

public interface UserService {
    public User createUser(String userName);

    public User subscribeTopic(SubscribedTopicDTO suscribedTopicDto) throws Exception;

    public List<IndividualAlert> getAlertsByUser(String nombreUsuario) throws Exception;

    public List<User> getUsers();
}
