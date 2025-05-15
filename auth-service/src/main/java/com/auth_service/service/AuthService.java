package com.auth_service.service;

import com.auth_service.dto.AuthRequest;
import com.auth_service.dto.AuthResponse;

public interface AuthService {
    String registerUser(AuthRequest request);
    AuthResponse loginUser(AuthRequest request);
}
