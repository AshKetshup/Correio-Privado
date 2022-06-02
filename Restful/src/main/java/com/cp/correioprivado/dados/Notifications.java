package com.cp.correioprivado.dados;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String message;
    @NonNull
    private boolean isRead;
    @NonNull
    @ManyToOne
    private News news;
    @NonNull
    @ManyToOne
    private User user;
}
