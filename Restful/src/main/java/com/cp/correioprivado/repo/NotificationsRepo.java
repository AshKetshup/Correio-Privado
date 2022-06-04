package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationsRepo extends JpaRepository<Notifications,Long> {
    Optional<Notifications> findById(Long id);
    List<Notifications> findAllByUserId(Long id);
}
