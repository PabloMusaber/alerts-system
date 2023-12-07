package com.alerts.services.Topic;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alerts.entities.Topic;
import com.alerts.repositories.TopicRepository;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    public TopicServiceImpl (TopicRepository topicRepository){
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic createTopic(Topic topic) {
        Topic newTopic = new Topic();
        newTopic.setTopicName(topic.getTopicName());
        topicRepository.addTopic(newTopic);
        return newTopic;
    }

    @Override
    public List<Topic> getTopics() {
        return topicRepository.getTopics();
    }

}
