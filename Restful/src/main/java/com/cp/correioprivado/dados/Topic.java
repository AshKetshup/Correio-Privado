package com.cp.correioprivado.dados;

import lombok.*;

import javax.persistence.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @OneToMany (targetEntity = TopicSubscribed.class, mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <TopicSubscribed> topicSubscribed;
    @OneToMany (targetEntity = News.class, mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List <News> news;
}
