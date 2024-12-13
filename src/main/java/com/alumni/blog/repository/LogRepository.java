package com.alumni.blog.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.alumni.blog.entities.Log;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}
