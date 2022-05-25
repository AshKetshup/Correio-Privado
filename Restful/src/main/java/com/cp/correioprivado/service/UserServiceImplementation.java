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
        return newsRepo.save(news);
    }

    @Override
    public Topic saveTopic(Topic topic) {
        log.info("Saving new topic {} to the database!", topic.getTitle());
        return topicRepo.save(topic);
    }

    @Override
    public void addRoleToUser(String username, String name) {
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(name);
        user.setRole_id(role.getId());
        log.info("Saving adding role {} to user {}!",role.getName(),user.getName());
    }

    @Override
    public Topic_Subscribed subscribeTopic(String username, String title){
        User user = userRepo.findByUsername(username);
        Topic topic = topicRepo.findByTitle(title);
        Topic_Subscribed topic_subscribed = new Topic_Subscribed(user.getId(),topic.getId());
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
        return newsRepo.findByTopic_id(topic1.getId());
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
    public List<Topic> getTopics() {
        return topicRepo.findAll();
    }

    @Override
    public List<News> getNews() {
        return newsRepo.findAll();
    }
    
    @Override
    public void removeTopicSubscribed(String username, String title){
        User user = userRepo.findByUsername(username);
        Topic topic = topicRepo.findByTitle(title);
        Topic_Subscribed topicSubscribed = topicSubscribedRepo.findByTopic_idAndUser_id(topic.getId(), user.getId());
        topicSubscribedRepo.delete(topicSubscribed);
    }
}
