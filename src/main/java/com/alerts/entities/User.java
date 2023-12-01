package com.alerts.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userName;

    private Set<Topic> suscribedTopics = new HashSet<>();

    private List<IndividualAlert> alerts = new ArrayList<>();

    public void subscribeTopic(Topic topic) {
        suscribedTopics.add(topic);
    }
}
