package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.Topic_Subscribed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicSubscribedRepo extends JpaRepository<Topic_Subscribed,Long> {
    Optional<Topic_Subscribed> findById(Long id);
    Topic_Subscribed findByTopic_idAndUser_id(Long topic_id, Long user_id);
    List<Topic_Subscribed> findByUserId(long id);
}
