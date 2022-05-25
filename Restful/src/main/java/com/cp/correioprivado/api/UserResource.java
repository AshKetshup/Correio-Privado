package com.cp.correioprivado.api;

import com.cp.correioprivado.dados.*;
import com.cp.correioprivado.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>saveRole(@RequestBody RoleToUserForm form){
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

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

    @PostMapping("/topic_subscribed/subscribe")
    public ResponseEntity<Topic_Subscribed>subscribeTopic(@RequestBody TopicSubscribeForm form){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/topic_subscribed/subscribe").toUriString());
        return ResponseEntity.created(uri).body(userService.subscribeTopic(form.getUsername(),form.getTitle()));
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

