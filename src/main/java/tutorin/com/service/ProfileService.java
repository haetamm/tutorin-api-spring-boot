package tutorin.com.service;

import tutorin.com.entities.request.profile.ProfileRequest;
import tutorin.com.entities.response.profile.ProfileResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;

public interface ProfileService {
    ProfileResponse updateProfile(ProfileRequest request) throws NotFoundException, ValidationCustomException;
    ProfileResponse getProfile() throws NotFoundException;
}
