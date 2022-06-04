package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = {"id","username", "email"})})
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
    @Column(nullable = true, length = 64)
    private String photo;

    public String getPhotoImagePath() {
        if (photo == null || id == null) return null;

        return "/user-photos/" + id + "/" + photo;
    }
}
