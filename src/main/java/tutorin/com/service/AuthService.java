package tutorin.com.service;

import tutorin.com.entities.user.*;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.ValidationCustomException;

public interface AuthService {
    RegisterResponse registerStudent(RegisterRequest request);
    RegisterResponse registerTutor(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    String forgetPassword(ForgotPasswordRequest request) throws ValidationCustomException;
    String resetPassword(String token, ResetPasswordRequest request) throws ValidationCustomException, BadRequestException;
}
