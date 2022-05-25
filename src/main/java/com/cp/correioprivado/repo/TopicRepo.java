package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepo extends JpaRepository<Topic,String> {
    Topic findByTitle(String title);
}
