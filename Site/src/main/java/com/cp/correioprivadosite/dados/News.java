package com.cp.correioprivadosite.dados;

import org.json.JSONObject;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;

public class News {
    private Long id;
    private String title;
    private String content;
    private String releaseDate;
    private User user;
    private Topic topic;

    public News() {    }

    public News(JSONObject obj) {
        this.id          = obj.getLong("id");
        this.title       = obj.getString("title");
        this.content     = obj.getString("content");
        this.releaseDate = obj.getString("releaseDate");
        this.user        = new User(obj.getJSONObject("user"));
        this.topic       = new Topic(obj.getJSONObject("topic"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
