package com.opencode.alumxbackend.users.service;

import com.opencode.alumxbackend.users.dto.UserProfileDTO;
import com.opencode.alumxbackend.users.dto.UserRequest;
import com.opencode.alumxbackend.users.model.User;

import java.util.Optional;

public interface UserService {
    User createUser(UserRequest request);
    Optional<UserProfileDTO> getUserProfile(Long id);
}
