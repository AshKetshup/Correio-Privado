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
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "roleId", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;
    @OneToMany(targetEntity = TopicSubscribed.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TopicSubscribed> topics;
    @OneToMany(targetEntity = News.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <News> news;
    @OneToMany(targetEntity = Notifications.class, mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <Notifications> notifications;
}
