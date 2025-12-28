package com.opencode.alumxbackend.jobposts.service;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.opencode.alumxbackend.common.exception.Errors.BadRequestException;
import com.opencode.alumxbackend.common.exception.Errors.ForbiddenException;
import com.opencode.alumxbackend.common.exception.Errors.ResourceNotFoundException;
import com.opencode.alumxbackend.jobposts.dto.JobPostRequest;
import com.opencode.alumxbackend.jobposts.dto.JobPostResponse;
import com.opencode.alumxbackend.jobposts.model.JobPost;
import com.opencode.alumxbackend.jobposts.repository.JobPostRepository;
import com.opencode.alumxbackend.users.model.User;
import com.opencode.alumxbackend.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;



// different from interface as here we are going to implement what we need
@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService{
    private final JobPostRepository jobPostRepository;
    private final UserRepository userRepository;

    @Override
    public List<JobPostResponse> getPostsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id  not found " + userId));

        List<JobPost> posts = jobPostRepository.findByUsernameOrderByCreatedAtDesc(user.getUsername());
        return JobPostResponse.fromEntities(posts);
    }

    @Override
    public JobPost createJobPost(JobPostRequest request) {



        if(!userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username does not exist: " + request.getUsername());
        }

        if(request.getDescription().length()>5000 || request.getDescription().isBlank() || request.getDescription().length() < 50){
            throw new IllegalArgumentException("Description must be between 1 and 5000 characters");
        }

        if(request.getImageUrls()!=null  && !request.getImageUrls().isEmpty()){

//        if(request.getImageUrls().size()>5){
//            throw new IllegalArgumentException("Cannot upload more than 5 images");
//        }
            request.getImageUrls().forEach(url->{


                try{
                    URI.create(url).toURL(); // this will chck whether url is correct
                }
                catch(IllegalArgumentException | MalformedURLException e){
                    throw new BadRequestException("invalid url: " + url);
                }
            });
        }
        JobPost jobPost = JobPost.builder()
                .postId(UUID.randomUUID().toString())
                .username(request.getUsername())
                .description(request.getDescription())
                .imageUrls(request.getImageUrls())
                .createdAt(LocalDateTime.now())
                .build();


        return jobPostRepository.save(jobPost);
    }

    @Override
    public void deletePostByUser(Long userId, String postId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the id " + userId));

        JobPost post = jobPostRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with postId " + postId));

        if (!post.getUsername().equals(user.getUsername())) {
            throw new ForbiddenException("User is not the owner of the post");
        }

        jobPostRepository.delete(post);
    }
}
