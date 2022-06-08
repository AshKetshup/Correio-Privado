package com.cp.correioprivado.dados;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name="news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String content;
    @NonNull
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private Date releaseDate;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "topicID")
    private Topic topic;
}