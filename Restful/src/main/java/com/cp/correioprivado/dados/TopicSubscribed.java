package com.cp.correioprivado.dados;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="topic_subscribed")
public class TopicSubscribed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "topicId")
    private Topic topic;
}
