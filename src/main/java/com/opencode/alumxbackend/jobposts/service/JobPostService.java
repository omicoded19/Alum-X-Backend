package com.opencode.alumxbackend.jobposts.service;

import com.opencode.alumxbackend.jobposts.dto.JobPostRequest;
import com.opencode.alumxbackend.jobposts.dto.JobPostResponse;
import com.opencode.alumxbackend.jobposts.model.JobPost;

import java.util.List;

public interface JobPostService {
    JobPost createJobPost(JobPostRequest request);
    void deletePostByUser(Long userId, String postId);
    List<JobPostResponse> getPostsByUser(Long userId);
    void likePost(String postId, Long userId);
}
