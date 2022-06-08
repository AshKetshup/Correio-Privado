package com.cp.correioprivado.api;

import com.cp.correioprivado.dados.*;
import com.cp.correioprivado.repo.NewsRepo;
import com.cp.correioprivado.repo.RoleRepo;
import com.cp.correioprivado.repo.TopicRepo;
import com.cp.correioprivado.repo.UserRepo;
import com.cp.correioprivado.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import javax.management.Notification;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final UserRepo userRepo;
    private final NewsRepo newsRepo;
    private final TopicRepo topicRepo;
    private final RoleRepo roleRepo;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok().body(userService.getRoles());
    }

    @GetMapping("/topicssubscribed")
    public ResponseEntity<List<TopicSubscribed>> getTopicsSubscribed() {
        return ResponseEntity.ok().body(userService.getTopicSubscribed());
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notifications>> getNotifications() {
        return ResponseEntity.ok().body(userService.getNotifications());
    }

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getTopics() {
        return ResponseEntity.ok().body(userService.getTopics());
    }

    @GetMapping("/news")
    public ResponseEntity<List<News>> getNews() {
        return ResponseEntity.ok().body(userService.getNews());
    }

    @PostMapping("/user/save")
    public RedirectView saveUser(
        @RequestParam String name,
        @RequestParam String surname,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String role
    ) {

        User user = new User(name, surname, email, password, roleRepo.findByName(role));
        userService.saveUser(user);
        return new RedirectView("/users", true);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestParam String title, @RequestParam String description) {

        Role role = new Role(title, description);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @GetMapping("/newsByTopic")
    public ResponseEntity<List<News>> getNewByTopic(@RequestParam String topic) {
        return ResponseEntity.ok().body(userService.getNewsByTopic(topic));
    }

    @GetMapping("/newsByUser")
    public ResponseEntity<List<News>> getNewByUser(@RequestParam String id) {
        return ResponseEntity.ok().body(userService.getNewsByUser(Long.parseLong(id)));
    }

    @GetMapping("/newsById")
    public ResponseEntity<News> getNewById(@RequestParam String id) {
        return ResponseEntity.ok().body(newsRepo.findById(Long.parseLong(id)));
    }

    @GetMapping("/newsBetweenDateByTopic")
    public ResponseEntity<List<News>> getNewsBetweenDatesByTopic(
        @RequestParam String topicid,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
        Date InitialDate,
        @RequestParam
        @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
        Date FinalDate
    ) {
        List<News> news = newsRepo.findAllByTopicId(Long.parseLong(topicid));
        List<News> selectedNews = new ArrayList<>();

        for (News value : news)
            if (InitialDate.before(value.getReleaseDate()) && FinalDate.after(value.getReleaseDate()))
                selectedNews.add(value);

        return ResponseEntity.ok().body(selectedNews);
    }

    @GetMapping("/userByEmail")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @GetMapping("/user/getRole")
    public ResponseEntity<Role> getRoleByEmail(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.getRoleByUser(email));
    }

    @PostMapping("/news/save")
    public RedirectView saveNews(
        @RequestParam String title,
        @RequestParam String content,
        @RequestParam String email,
        @RequestParam String topic
    ) {
        News news = new News(title, content, new Date(), userRepo.findByEmail(email), topicRepo.findByTitle(topic));
        userService.saveNews(news);

        return new RedirectView("/news", true);
    }

    @PostMapping("/topic/save")
    public ResponseEntity<Topic> saveTopic(@RequestParam String title, @RequestParam String description) {

        Topic topic = new Topic(title, description);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/topic/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveTopic(topic));
    }

    @DeleteMapping("/topic/remove")
    public ResponseEntity<String> removeTopic(@RequestParam String title) {
        userService.removeTopic(title);
        return ResponseEntity.ok(title);
    }

    @PostMapping("/topic_subscribed/subscribe")
    public ResponseEntity<TopicSubscribed> subscribeTopic(@RequestParam String email, @RequestParam String title) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/topic_subscribed/subscribe").toUriString());
        return ResponseEntity.created(uri).body(userService.subscribeTopic(email, title));
    }

    @DeleteMapping("/topic_subscribed/unsubscribe")
    public ResponseEntity<String> removeTopicSubscribed(@RequestParam String email,
                                                        @RequestBody String title) {
        userService.removeTopicSubscribed(email, title);
        return ResponseEntity.ok(title);
    }

    @GetMapping("/topic_subscribedByUser")
    public ResponseEntity<List<TopicSubscribed>> getTopicsSubscribedByUser(@RequestParam String id) {
        return ResponseEntity.ok().body(userService.getTopicsSubscribedByUser(Long.parseLong(id)));
    }

    @PostMapping("/notifications/save")
    public ResponseEntity<Notifications> saveNotifications(
        @RequestParam(required = false)
        String message,
        @RequestParam String idnews,
        @RequestParam String iduser
    ) {
        Notifications notification = new Notifications(
            message,
            false,
            newsRepo.findById(Long.parseLong(idnews)),
            userRepo.findById(Long.parseLong(iduser))
        );

        URI uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/notifications/save")
                .toUriString()
        );

        return ResponseEntity.created(uri).body(userService.saveNotification(notification));
    }

    @DeleteMapping("/notifications/remove")
    public ResponseEntity<Long> removeNotification(@RequestParam String id) {
        userService.removeNotification(Long.parseLong(id));
        return ResponseEntity.ok(Long.parseLong(id));
    }

    @GetMapping("/notificationsByUser")
    public ResponseEntity<List<Notifications>> getNotificationsByUser(@RequestParam String id) {
        return ResponseEntity.ok().body(userService.getNotificationsByUser(Long.parseLong(id)));
    }
}

