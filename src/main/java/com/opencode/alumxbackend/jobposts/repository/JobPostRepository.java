package com.opencode.alumxbackend.jobposts.repository;

import com.opencode.alumxbackend.jobposts.model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, String> {
    List<JobPost> findByUsernameOrderByCreatedAtDesc(String username);
}
