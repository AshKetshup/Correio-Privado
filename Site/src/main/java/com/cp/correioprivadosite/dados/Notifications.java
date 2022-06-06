package com.cp.correioprivadosite.dados;

import org.json.JSONObject;

import java.text.ParseException;

public class Notifications {
    private Long id;
    private String message;
    private boolean isRead;
    private News news;
    private User user;

    public Notifications (JSONObject obj) throws ParseException {
        this.id = obj.getLong("id");
        this.message = obj.getString("message");
        this.isRead = obj.getBoolean("isRead");
        this.news = new News (obj.getJSONObject("news"));
        this.user = new User (obj.getJSONObject("user"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}