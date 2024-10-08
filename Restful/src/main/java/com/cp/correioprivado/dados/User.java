package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = {"id","surname", "email"})})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private String email;
    @NonNull
    private String password; // password can be salted with BCryptPasswordEncoder
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "roleId")
    private Role role;
}
