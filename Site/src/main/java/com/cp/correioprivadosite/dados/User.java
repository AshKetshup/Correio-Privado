package com.cp.correioprivadosite.dados;

import org.json.JSONObject;

public class User {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password; // password can be salted with BCryptPasswordEncoder
    private Role role;
//    private String photo;

    public User(JSONObject obj) {
        this.id       = obj.getLong("id");
        this.name     = obj.getString("name");
        this.surname  = obj.getString("surname");
        this.email    = obj.getString("email");
        this.password = obj.getString("password");
        this.role     = new Role(obj.getJSONObject("role"));
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
