package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,String> {
    User findBySurname(String surname);
    User findById(Long id);
}
