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
    @ManyToOne
    private User user;
    @NonNull
    @ManyToOne
    private Topic topic;
}
