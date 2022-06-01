package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

import java.sql.Date;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

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
    private Date release_date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Topic topic;
    @OneToMany (mappedBy = "news")
    private List<Notifications> notifications;
}