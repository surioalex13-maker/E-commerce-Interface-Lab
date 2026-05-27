package com.shopease.service;

import com.shopease.entity.User;
import com.shopease.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User createUser(String email, String password, String fullName) throws Exception {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception("User already exists with this email");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .role("USER")
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow();
        if (userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
        if (userDetails.getPhone() != null) user.setPhone(userDetails.getPhone());
        if (userDetails.getAddress() != null) user.setAddress(userDetails.getAddress());
        return userRepository.save(user);
    }
}
