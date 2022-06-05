package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.News;
import com.cp.correioprivado.dados.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepo extends JpaRepository<News,String> {
    News findByTitle(String title);
    List<News> findAllByTopicId(Long topicId);
    List<News> findAllByUser(User user);
    News findById(Long id);
}
