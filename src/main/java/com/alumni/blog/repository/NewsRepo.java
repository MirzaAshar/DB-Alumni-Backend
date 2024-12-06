package com.alumni.blog.repository;

import com.alumni.blog.entities.News;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NewsRepo extends JpaRepository<News, Long> {

}
