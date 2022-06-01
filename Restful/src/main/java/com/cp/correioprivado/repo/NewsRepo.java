package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepo extends JpaRepository<News,String> {
    News findByTitle(String title);
    News findByTopicId(Long topicId);
}
