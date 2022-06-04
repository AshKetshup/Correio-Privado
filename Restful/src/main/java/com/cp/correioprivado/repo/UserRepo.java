package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
    User findByEmail(String email);
    User findById(Long id);
}
