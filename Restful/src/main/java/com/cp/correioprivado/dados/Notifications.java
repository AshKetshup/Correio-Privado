package com.cp.correioprivado.dados;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String message;
    @NonNull
    private boolean isRead;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "newsId")
    private News news;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
}
