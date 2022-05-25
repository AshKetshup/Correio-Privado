package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity @Data @NoArgsConstructor @AllArgsConstructor @RequiredArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private User user;
}
