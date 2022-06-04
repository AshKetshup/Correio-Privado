package com.cp.correioprivado.service;

import com.cp.correioprivado.dados.*;
import com.cp.correioprivado.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final NewsRepo newsRepo;
    private final TopicRepo topicRepo;
    private final PasswordEncoder passwordEncoder;
    private final NotificationsRepo notificationsRepo;

    private final TopicSubscribedRepo topicSubscribedRepo;
    @Override
    public User saveUser(User user, MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        user.setPhoto(fileName);
        User savedUser = userRepo.save(user);
        log.info("Saving new photo {} to the database!", fileName);
        String uploadDir = "user-photos/" + savedUser.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        log.info("Saving new user {} to the database!", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return savedUser;
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database!", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

        List<TopicSubscribed> listSubscriptions = topicSubscribedRepo.findAllByTopicId(news.getTopic().getId());
        for(int i = 0; i < listSubscriptions.size(); i++){
            saveNotification(new Notifications(
                    "Notícia nova no tópico: " + news.getTopic().getTitle(), false, news, listSubscriptions.get(i).getUser()));
        }
        return newsRepo.save(news);
    }

    @Override
    public News saveNews(News news, MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        news.setPhoto(fileName);
        News savedNews = newsRepo.save(news);
        log.info("Saving new photo {} to the database!", fileName);
        String uploadDir = "news-photos/" + savedNews.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return savedNews;
    }

    @Override
    public Topic saveTopic(Topic topic) {
        log.info("Saving new topic {} to the database!", topic.getTitle());
        return topicRepo.save(topic);
    }

    @Override
    public TopicSubscribed subscribeTopic(String email, String title){
        User user = userRepo.findByEmail(email);
        Topic topic = topicRepo.findByTitle(title);
        TopicSubscribed topic_subscribed = new TopicSubscribed(user,topic);
        log.info("Subscribing topic {} to user {}!", topic.getTitle(), user.getName());
        return topicSubscribedRepo.save(topic_subscribed);
    }

    @Override
    public User getUser(String email) {
        log.info("Getting user {}!",email);
        return userRepo.findByEmail(email);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Getting user {}!", id);
        return userRepo.findById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public News getNewsByTitle(String title) {
        log.info("Getting news {}!", title);
        return newsRepo.findByTitle(title);
    }

    @Override
    public List<News> getNewsByTopic(String topic) {
        log.info("Getting news by {}!", topic);
        Topic topic1 = topicRepo.findByTitle(topic);
        return newsRepo.findAllByTopicId(topic1.getId());
    }

    @Override
    public List<News> getNewsByUser(Long id) {
        log.info("Getting news by id: {}!", id);
        User user = userRepo.findById(id);
        return newsRepo.findAllByUser(user);
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
    public Role getRoleByUser(String email) {
        return userRepo.findByEmail(email).getRole();
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
    public void removeTopicSubscribed(String email, String title){
        User user = userRepo.findByEmail(email);
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
        return topicSubscribedRepo.findAllByUserId(id);
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
        return notificationsRepo.findAllByUserId(id);
    }

    @Override
    public void removeNotification(long id) {
        log.info("Deleting notification: {}!", notificationsRepo.findById(id));
        notificationsRepo.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if(user == null) {
            log.error("User not Found");
            throw new UsernameNotFoundException("User not found in the db");
        } else {
            log.info("User Found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
