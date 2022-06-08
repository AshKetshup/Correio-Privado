package com.cp.correioprivado.service;

import com.cp.correioprivado.dados.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    // USER
    User saveUser(User user);
    List<User> getUsers();
    User getUser(String username);
    User getUserById(Long id);
    User getUserByEmail(String email);

    // ROLE
    Role saveRole(Role role);
    List<Role> getRoles();
    Role getRoleByUser(String username);

    // NEWS
    News saveNews(News news);
    List<News> getNews();
    News getNewsByTitle(String title);
    List<News> getNewsByTopic(String topic);
    List<News> getNewsByUser(Long id);

    // TOPIC
    Topic saveTopic(Topic topic);
    List<Topic> getTopics();
    void removeTopic(String title);

    // TOPIC_SUBSCRIBED
    TopicSubscribed subscribeTopic(String username, String topic);
    void removeTopicSubscribed(String username, String title);
    List<TopicSubscribed> getTopicSubscribed();
    List<TopicSubscribed> getTopicsSubscribedByUser(Long id);

    // NOTIFICATION
    Notifications saveNotification(Notifications notification);
    List<Notifications> getNotifications();
    List<Notifications> getNotificationsByUser(Long id);
    void removeNotification(long id);

    // FILE MANAGEMENT
}

