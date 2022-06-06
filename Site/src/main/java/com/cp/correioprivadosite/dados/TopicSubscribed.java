package com.cp.correioprivadosite.dados;

import org.json.JSONObject;

import java.text.ParseException;

public class TopicSubscribed {
    private Long id;
    private User user;
    private Topic topic;

    public TopicSubscribed(JSONObject obj) throws ParseException {
        this.id = obj.getLong("id");
        this.user = new User(obj.getJSONObject("user"));
        this.topic = new Topic(obj.getJSONObject("topic"));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
