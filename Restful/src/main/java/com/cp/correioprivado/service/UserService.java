package com.cp.correioprivado.service;

import com.cp.correioprivado.dados.News;
import com.cp.correioprivado.dados.Role;
import com.cp.correioprivado.dados.Topic;
import com.cp.correioprivado.dados.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    News saveNews(News news);
    Topic saveTopic(Topic topic);
    void addRoleToUser(String username, String name);
    User getUser(String username);
    News getNews(String title);
    List<User> getUsers();
    List<Role> getRoles();
    List<Topic> getTopics();
    List<News> getNews();
}

