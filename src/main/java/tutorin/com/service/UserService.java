package tutorin.com.service;

import org.springframework.web.multipart.MultipartFile;
import tutorin.com.entities.file.FileResponse;
import tutorin.com.entities.user.ChangePasswordRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.model.User;

import java.io.IOException;

public interface UserService {
    User getByUserId(String id) throws NotFoundException;
    String changePassword(ChangePasswordRequest request) throws NotFoundException, ValidationCustomException;
    FileResponse uploadImage(MultipartFile imageRequest) throws NotFoundException, BadRequestException, IOException;
    FileResponse uploadResume(MultipartFile request) throws NotFoundException, BadRequestException, IOException;
}
