package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.TopicSubscribed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicSubscribedRepo extends JpaRepository<TopicSubscribed,Long> {
    Optional<TopicSubscribed> findById(Long id);
    TopicSubscribed findByTopicIdAndUserId(Long topicId, Long userId);
    List<TopicSubscribed> findAllByTopicId(Long id);
    List<TopicSubscribed> findAllByUserId(long id);
}
