package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.LoginRequest;
import com.hotelmanagement.dto.request.RegisterRequest;
import com.hotelmanagement.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    AuthResponse refresh(String refreshToken);
}
