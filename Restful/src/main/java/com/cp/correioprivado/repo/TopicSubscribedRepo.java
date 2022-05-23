package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.Topic_Subscribed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicSubscribedRepo extends JpaRepository<Topic_Subscribed,String> {
    Optional<Topic_Subscribed> findById(String id);
}
