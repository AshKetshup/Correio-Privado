package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    private Date releaseDate;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "topicID", referencedColumnName = "id", insertable = false, updatable = false)
    private Topic topic;
    @OneToMany (targetEntity = Notifications.class, mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notifications> notifications;
}