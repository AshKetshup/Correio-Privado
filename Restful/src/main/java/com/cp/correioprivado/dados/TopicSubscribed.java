package com.cp.correioprivado.dados;


import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class TopicSubscribed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "topicId", referencedColumnName = "id", insertable = false, updatable = false)
    private Topic topic;
}
