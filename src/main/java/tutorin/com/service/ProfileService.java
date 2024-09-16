package tutorin.com.service;

import org.springframework.web.multipart.MultipartFile;
import tutorin.com.entities.image.ImageResponse;
import tutorin.com.entities.profile.ProfileRequest;
import tutorin.com.entities.profile.ProfileResponse;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import java.io.IOException;

public interface ProfileService {
    ProfileResponse updateProfile(ProfileRequest request) throws NotFoundException, ValidationCustomException;
    ProfileResponse getProfile() throws NotFoundException;
    ImageResponse upload(MultipartFile imageRequest) throws NotFoundException, BadRequestException, IOException;
}
