package tutorin.com.service;

import tutorin.com.entities.user.ChangePasswordRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.model.User;

public interface UserService {
    User getByUserId(String id) throws NotFoundException;
    String changePassword(ChangePasswordRequest request) throws NotFoundException, ValidationCustomException;
}
