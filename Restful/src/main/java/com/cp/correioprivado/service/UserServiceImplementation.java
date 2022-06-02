package com.cp.correioprivado.service;

import com.cp.correioprivado.dados.*;
import com.cp.correioprivado.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImplementation implements UserService{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final NewsRepo newsRepo;
    private final TopicRepo topicRepo;
    private final NotificationsRepo notificationsRepo;

    private final TopicSubscribedRepo topicSubscribedRepo;
    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database!", user.getName());
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database!", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public News saveNews(News news) {
        log.info("Saving new news {} to the database!", news.getTitle());

        List<TopicSubscribed> listSubscriptions = topicSubscribedRepo.findByTopicId(news.getTopic().getId());
        for(int i = 0; i < listSubscriptions.size(); i++){
            saveNotification(new Notifications(
                    "Notícia nova no tópico: " + news.getTopic().getTitle(), false, news, listSubscriptions.get(i).getUser()));
        }
        return newsRepo.save(news);
    }

    @Override
    public Topic saveTopic(Topic topic) {
        log.info("Saving new topic {} to the database!", topic.getTitle());
        return topicRepo.save(topic);
    }

    @Override
    public TopicSubscribed subscribeTopic(String username, String title){
        User user = userRepo.findByUsername(username);
        Topic topic = topicRepo.findByTitle(title);
        TopicSubscribed topic_subscribed = new TopicSubscribed(user,topic);
        log.info("Subscribing topic {} to user {}!", topic.getTitle(), user.getName());
        return topicSubscribedRepo.save(topic_subscribed);
    }

    @Override
    public User getUser(String username) {
        log.info("Getting user {}!",username);
        return userRepo.findByUsername(username);
    }

    @Override
    public News getNewsByTitle(String title) {
        log.info("Getting news {}!", title);
        return newsRepo.findByTitle(title);
    }

    @Override
    public News getNewsByTopic(String topic) {
        log.info("Getting news {}!", topic);
        Topic topic1 = topicRepo.findByTitle(topic);
        return newsRepo.findByTopicId(topic1.getId());
    }

    @Override
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Role getRoleByUser(String username) {
        return userRepo.findByUsername(username).getRole();
    }

    @Override
    public List<Topic> getTopics() {
        return topicRepo.findAll();
    }

    @Override
    public void removeTopic(String title) {
        log.info("Deleting topic: {}!", topicRepo.findById(title));
        topicRepo.deleteById(title);
    }

    @Override
    public List<News> getNews() {
        return newsRepo.findAll();
    }
    
    @Override
    public void removeTopicSubscribed(String username, String title){
        User user = userRepo.findByUsername(username);
        Topic topic = topicRepo.findByTitle(title);
        TopicSubscribed topicSubscribed = topicSubscribedRepo.findByTopicIdAndUserId(topic.getId(), user.getId());
        log.info("Deleting subscribed topic: {}!", topicSubscribed.getTopic().getTitle());
        topicSubscribedRepo.deleteById(topicSubscribed.getId());
    }

    @Override
    public List<TopicSubscribed> getTopicSubscribed() {
        return topicSubscribedRepo.findAll();
    }

    @Override
    public List<TopicSubscribed> getTopicsSubscribedByUser(Long id){
        return topicSubscribedRepo.findByUserId(id);
    }
    @Override
    public Notifications saveNotification(Notifications notification) {
        log.info("Saving new notification {} to the database!", notification.getMessage());
        return  notificationsRepo.save(notification);
    }

    @Override
    public List<Notifications> getNotifications() {
        return notificationsRepo.findAll();
    }
    @Override
    public List<Notifications> getNotificationsByUser(Long id) {
        return notificationsRepo.findByUserId(id);
    }

    @Override
    public void removeNotification(long id) {
        log.info("Deleting notification: {}!", notificationsRepo.findById(id));
        notificationsRepo.deleteById(id);
    }
}
