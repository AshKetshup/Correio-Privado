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
    @OneToMany (mappedBy = "topic")
    private List <TopicSubscribed> topicSubscribed;
    @OneToMany(mappedBy = "topic")
    private List <News> news;
}
