package com.cp.correioprivado.repo;

import com.cp.correioprivado.dados.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepo extends JpaRepository<News,Long> {
    News findByTitle(String title);
}
