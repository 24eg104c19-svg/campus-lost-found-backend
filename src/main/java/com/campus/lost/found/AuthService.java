package com.campus.lost.found;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser register(AppUser user) {

        if (user.getEmail() == null ||
            user.getEmail().isBlank() ||
            user.getPassword() == null ||
            user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        String email = user.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Public registration cannot assign ADMIN role.
        user.setRole("USER");

        return userRepository.save(user);
    }

    public AppUser login(String email, String password) {

        if (email == null || password == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        AppUser user = userRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() ->
                    new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }
}