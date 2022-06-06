package com.cp.correioprivadosite.dados;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
@Slf4j
public class Topic {
    private Long id;
    private String title;
    private String description;

    public Topic(JSONObject obj) {
        log.debug(obj.toString());

        this.id          = obj.getLong("id");
        this.title       = obj.getString("title");
        this.description = obj.getString("description");
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}