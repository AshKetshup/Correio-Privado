package com.cp.correioprivado.api;

import com.cp.correioprivado.dados.*;
import com.cp.correioprivado.service.UserService;
import com.sun.nio.sctp.Notification;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>>getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>>getRoles(){
        return ResponseEntity.ok().body(userService.getRoles());
    }

    @GetMapping("/topicssubscribed")
    public ResponseEntity<List<TopicSubscribed>>getTopicsSubscribed(){
        return ResponseEntity.ok().body(userService.getTopicSubscribed());
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notifications>>getNotifications(){
        return ResponseEntity.ok().body(userService.getNotifications());
    }

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>>getTopics(){
        return ResponseEntity.ok().body(userService.getTopics());
    }
    @GetMapping("/news")
    public ResponseEntity<List<News>>getNews(){
        return ResponseEntity.ok().body(userService.getNews());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User>saveUser(@RequestBody User user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/newsByTopic")
    public ResponseEntity<News>getNewByTopic(@RequestBody String topic){
        return ResponseEntity.ok().body(userService.getNewsByTopic(topic));
    }

    @PostMapping("/user/getRole")
    public ResponseEntity<Role>getRoleByUsername(@RequestBody String username){
        return ResponseEntity.ok().body(userService.getRoleByUser(username));
    }
//    @PostMapping("/role/addtouser")
//    public ResponseEntity<?>saveRole(@RequestBody RoleToUserForm form){
//        userService.addRoleToUser(form.getUsername(), form.getRoleName());
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/news/save")
    public ResponseEntity<News>saveNews(@RequestBody News news){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/news/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveNews(news));
    }

    @PostMapping("/topic/save")
    public ResponseEntity<Topic>saveTopic(@RequestBody Topic topic){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/topic/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveTopic(topic));
    }

    @DeleteMapping("/topic/remove")
    public ResponseEntity<String>removeTopic(@RequestBody String title){
        userService.removeTopic(title);
        return ResponseEntity.ok(title);
    }

    @PostMapping("/topic_subscribed/subscribe")
    public ResponseEntity<TopicSubscribed>subscribeTopic(@RequestBody TopicSubscribeForm form){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/topic_subscribed/subscribe").toUriString());
        return ResponseEntity.created(uri).body(userService.subscribeTopic(form.getUsername(),form.getTitle()));
    }

    @DeleteMapping("/topic_subscribed/unsubscribe")
    public ResponseEntity<TopicSubscribeForm>removeTopicSubscribed(@RequestBody TopicSubscribeForm form){
        userService.removeTopicSubscribed(form.getUsername(),form.getTitle());
        return ResponseEntity.ok(form);
    }

    @GetMapping("/topic_subscribedByUser")
    public ResponseEntity<List<TopicSubscribed>>getTopicsSubscribedByUser(@RequestBody Long id){
        return ResponseEntity.ok().body(userService.getTopicsSubscribedByUser(id));
    }

    @PostMapping("/notifications/save")
    public ResponseEntity<Notifications>saveNotifications(@RequestBody Notifications notification){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/notifications/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveNotification(notification));
    }

    @DeleteMapping("/notifications/remove")
    public ResponseEntity<Long>removeNotification(@RequestBody Long id){
        userService.removeNotification(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/notificationsByUser")
    public ResponseEntity<List<Notifications>>getNotificationsByUser(@RequestBody Long id){
        return ResponseEntity.ok().body(userService.getNotificationsByUser(id));
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}

@Data
class TopicSubscribeForm{
    private String username;
    private String title;
}

