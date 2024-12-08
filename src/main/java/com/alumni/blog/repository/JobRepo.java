package com.alumni.blog.repository;

import com.alumni.blog.entities.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepo extends JpaRepository<JobPost,Integer> {

}
