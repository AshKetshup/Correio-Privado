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
    @ManyToOne()
    @JoinColumn(name = "newsId", referencedColumnName = "id", insertable = false, updatable = false)
    private News news;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
