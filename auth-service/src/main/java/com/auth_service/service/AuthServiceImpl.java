package com.auth_service.service;

import com.auth_service.dto.AuthRequest;
import com.auth_service.dto.AuthResponse;
import com.auth_service.entity.User;
import com.auth_service.exception.AuthenticationFailedException;
import com.auth_service.exception.ResourceAlreadyExistsException;
import com.auth_service.repository.UserRepository;
import com.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public String registerUser(AuthRequest request) {
        log.info("Registering user: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email is already taken");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();
        userRepository.save(user);
        log.info("User {} registered successfully", request.getEmail());
        return "User registered successfully";
    }

    @Override
    public AuthResponse loginUser(AuthRequest request) {
        log.info("User login attempt: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            log.error("Email not found: {}", request.getEmail());
            return new AuthenticationFailedException("Invalid Email or password");
        });
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Password mismatch for user: {}", request.getPassword());
            throw new AuthenticationFailedException("Invalid Email or password");
        }
        String token = jwtUtil.generateToken(request.getEmail());
        log.info("Token generated for user: {}", user.getEmail());
        return new AuthResponse(token);
    }
}
