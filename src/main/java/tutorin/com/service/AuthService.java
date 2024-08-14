package tutorin.com.service;

import tutorin.com.entities.request.user.RegisterRequest;
import tutorin.com.entities.response.user.RegisterResponse;

public interface AuthService {
    RegisterResponse registerStudent(RegisterRequest request);
    RegisterResponse registerTutor(RegisterRequest request);
}
