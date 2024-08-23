package com.suraev.TaskManagementSystem.service;

import com.suraev.TaskManagementSystem.dto.JwtAuthenticationResponse;
import com.suraev.TaskManagementSystem.dto.SignInRequest;
import com.suraev.TaskManagementSystem.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);
    public JwtAuthenticationResponse signIn(SignInRequest request);
}
