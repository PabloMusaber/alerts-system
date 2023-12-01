package com.alerts.services.Topic;

import java.util.List;

import com.alerts.entities.Topic;

public interface TopicService {

    public Topic createTopic(Topic topic);

    public List<Topic> getTopics();

}
