package com.cp.correioprivadosite.dados;

import org.json.JSONObject;

public class Role {
    private Long id;
    private String name;
    private String description;

    public Role(JSONObject obj) {
        this.id          = obj.getLong("id");
        this.name        = obj.getString("name");
        this.description = obj.getString("description");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
