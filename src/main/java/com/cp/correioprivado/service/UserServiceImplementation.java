package com.cp.correioprivado.service;

import com.cp.correioprivado.dados.News;
import com.cp.correioprivado.dados.Role;
import com.cp.correioprivado.dados.Topic;
import com.cp.correioprivado.dados.User;
import com.cp.correioprivado.repo.NewsRepo;
import com.cp.correioprivado.repo.RoleRepo;
import com.cp.correioprivado.repo.TopicRepo;
import com.cp.correioprivado.repo.UserRepo;
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
    public User getUser(String username) {
        log.info("Getting user {}!",username);
        return userRepo.findByUsername(username);
    }

    @Override
    public News getNews(String title) {
        log.info("Getting news {}|", title);
        return newsRepo.findByTitle(title);
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
}
