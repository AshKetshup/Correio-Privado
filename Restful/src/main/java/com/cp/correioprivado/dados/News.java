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
    private Date releaseDate;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
    @NonNull
    @ManyToOne()
    @JoinColumn(name = "topicID")
    private Topic topic;
    @Column(nullable = true, length = 64)
    private String photo;

    public String getPhotoImagePath() {
        if (photo == null || id == null) return null;

        return "/news-photos/" + id + "/" + photo;
    }
}