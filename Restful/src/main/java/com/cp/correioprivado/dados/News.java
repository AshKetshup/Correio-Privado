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
    @ManyToOne
    private User user;
    @NonNull
    @ManyToOne
    private Topic topic;
    @OneToMany (mappedBy = "news")
    private List<Notifications> notifications;
}