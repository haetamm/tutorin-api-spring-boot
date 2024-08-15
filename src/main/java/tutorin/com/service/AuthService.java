package tutorin.com.service;

import tutorin.com.entities.user.LoginRequest;
import tutorin.com.entities.user.RegisterRequest;
import tutorin.com.entities.user.LoginResponse;
import tutorin.com.entities.user.RegisterResponse;

public interface AuthService {
    RegisterResponse registerStudent(RegisterRequest request);
    RegisterResponse registerTutor(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
