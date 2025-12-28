package com.opencode.alumxbackend.users.service;

import com.opencode.alumxbackend.common.exception.Errors.BadRequestException;
import com.opencode.alumxbackend.users.dto.UserProfileDTO;
import com.opencode.alumxbackend.users.dto.UserRequest;
import com.opencode.alumxbackend.users.model.User;
import com.opencode.alumxbackend.users.model.UserRole;
import com.opencode.alumxbackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(UserRequest request) {

        // 1️⃣ Check uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists: " + request.getEmail());
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists: " + request.getUsername());
        }

        // 2️⃣ Validate role
        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role. Must be STUDENT, ALUMNI, or PROFESSOR.");
        }

        // 3️⃣ Optional: validate email format, password length etc.
        if (!request.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}(\\.[A-Za-z]{2,})?$")) {
            throw new BadRequestException("Invalid email format: " + request.getEmail());
        }


        if (request.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }

        // 4️⃣ Create and save user
        User user = User.builder()
                .username(request.getUsername())
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .profileCompleted(true) // default for dev
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<UserProfileDTO> getUserProfile(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserProfileDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.getEmail(),
                        user.getSkills(),
                        user.getEducation(),
                        user.getTechStack()

                ));
    }
}
