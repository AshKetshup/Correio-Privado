package com.cp.correioprivado.service;

import com.cp.correioprivado.dados.*;

import java.util.List;

public interface UserService {

    // USER
    User saveUser(User user);
    List<User> getUsers();
    User getUser(String username);
    void addRoleToUser(String username, String name);

    // ROLE
    Role saveRole(Role role);
    List<Role> getRoles();

    // NEWS
    News saveNews(News news);
    List<News> getNews();
    News getNewsByTitle(String title);
    News getNewsByTopic(String topic);

    // TOPIC
    Topic saveTopic(Topic topic);
    List<Topic> getTopics();

    // TOPIC_SUBSCRIBED
    TopicSubscribed subscribeTopic(String username, String topic);
    void removeTopicSubscribed(String username, String title);

    // NOTIFICATION
}

