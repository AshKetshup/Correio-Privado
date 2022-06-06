package com.cp.correioprivado.api;

import com.cp.correioprivado.dados.*;
import com.cp.correioprivado.repo.NewsRepo;
import com.cp.correioprivado.repo.TopicRepo;
import com.cp.correioprivado.repo.UserRepo;
import com.cp.correioprivado.service.UserService;
import com.sun.nio.sctp.Notification;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;
    private final UserRepo userRepo;
    private final NewsRepo newsRepo;
    private final TopicRepo topicRepo;

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

//    @PostMapping("/user/save")
//    public ResponseEntity<User>saveUser(@RequestBody User user){
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
//        return ResponseEntity.created(uri).body(userService.saveUser(user));
//    }

    @PostMapping("/user/save")
    public RedirectView saveUser(User user,
    @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if(multipartFile != null)
            userService.saveUser(user, multipartFile);
        else
            userService.saveUser(user);
        return new RedirectView("/users", true);
    }

    @GetMapping("/user/getImage")
    public ResponseEntity<String>getUserImage(@RequestBody String id){
        Optional<User> user = userRepo.findById(id);
        if (user.isEmpty())
            return ResponseEntity.ok().body("");
        else
            return ResponseEntity.ok().body(user.get().getPhotoImagePath());
    }

    @GetMapping("/news/getImage")
    public ResponseEntity<String>getNewsImage(@RequestBody String id){
        Optional<News> news = newsRepo.findById(id);
        if (news.isEmpty())
            return ResponseEntity.ok().body("");
        else
            return ResponseEntity.ok().body(news.get().getPhotoImagePath());
    }


    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @GetMapping("/newsByTopic")
    public ResponseEntity<List<News>>getNewByTopic(@RequestBody String topic){
        return ResponseEntity.ok().body(userService.getNewsByTopic(topic));
    }

    @GetMapping("/newsByUser")
    public ResponseEntity<List<News>>getNewByTopic(@RequestBody Long id){
        return ResponseEntity.ok().body(userService.getNewsByUser(id));
    }

    @GetMapping("/newsById")
    public ResponseEntity<News>getNewById(@RequestBody Long id){
        return ResponseEntity.ok().body(newsRepo.findById(id));
    }

    @GetMapping("/newsBetweenDateByTopic")
    public ResponseEntity<List<News>>getNewsBetweenDatesByTopic(@RequestBody Long topic, Date InitialDate, Date FinalDate){
        List<News> news = newsRepo.findAllByTopicId(topic);
        List<News> selectedNews = null;
        for (News value : news) {
            if (InitialDate.before(value.getReleaseDate()) && FinalDate.after(value.getReleaseDate()))
                selectedNews.add(value);
        }
        return ResponseEntity.ok().body(selectedNews);
    }

    @GetMapping("/userByEmail")
    public ResponseEntity<User>getUserByEmail(@RequestBody String email){
        return ResponseEntity.ok().body(userService.getUserByEmail(email));
    }

    @GetMapping("/user/getRole")
    public ResponseEntity<Role>getRoleByEmail(@RequestBody String email){
        return ResponseEntity.ok().body(userService.getRoleByUser(email));
    }

//    @PostMapping("/role/addtouser")
//    public ResponseEntity<?>saveRole(@RequestBody RoleToUserForm form){
//        userService.addRoleToUser(form.getUsername(), form.getRoleName());
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/news/save")
    public RedirectView saveNews(String title, String content, String email, String topic,
        @RequestParam("image") MultipartFile multipartFile) throws IOException {

        News news = new News(title,content,new Date(),userRepo.findByEmail(email),topicRepo.findByTitle(topic));

        if(multipartFile != null)
                userService.saveNews(news, multipartFile);
            else
                userService.saveNews(news);
            return new RedirectView("/news", true);
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
        return ResponseEntity.created(uri).body(userService.subscribeTopic(form.getEmail(),form.getTitle()));
    }

    @DeleteMapping("/topic_subscribed/unsubscribe")
    public ResponseEntity<TopicSubscribeForm>removeTopicSubscribed(@RequestBody TopicSubscribeForm form){
        userService.removeTopicSubscribed(form.getEmail(),form.getTitle());
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
    private String email;
    private String roleName;
}

@Data
class TopicSubscribeForm{
    private String email;
    private String title;
}

