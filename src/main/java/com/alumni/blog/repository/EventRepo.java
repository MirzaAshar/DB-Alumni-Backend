package com.alumni.blog.repository;

import com.alumni.blog.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Integer> {

}
