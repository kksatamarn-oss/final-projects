package com.example.loginapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalStateException("Username already exists!");
        }
        userRepository.save(new User(username, password));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void updateBio(Long userId, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found!"));
        user.setBio(bio);
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found!"));

        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalStateException("Old password does not match!");
        }

        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
