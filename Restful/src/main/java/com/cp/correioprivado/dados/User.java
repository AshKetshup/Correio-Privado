package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

import java.util.List;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id","username", "email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password; // password can be salted with BCryptPasswordEncoder
    @ManyToOne
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<TopicSubscribed> topics;
    @OneToMany(mappedBy = "user")
    private List <News> news;
    @OneToMany (mappedBy ="user")
    private List <Notifications> notifications;
}
