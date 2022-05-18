package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role findByName(String name);
}
