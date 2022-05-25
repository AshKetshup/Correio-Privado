package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
    User findByUsername(String username);
    User findByEmail(String email);
}
