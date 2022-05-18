package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationsRepo extends JpaRepository<Notifications,String> {
    Optional<Notifications> findById(String id);
}
