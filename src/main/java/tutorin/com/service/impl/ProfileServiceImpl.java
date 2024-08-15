package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.request.profile.ProfileRequest;
import tutorin.com.entities.response.profile.ProfileResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.model.Profile;
import tutorin.com.repository.ProfileRepository;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.ProfileService;
import tutorin.com.validation.ValidationUtil;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ValidationUtil validationUtil;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProfileResponse updateProfile(ProfileRequest request) throws NotFoundException, ValidationCustomException {
        validationUtil.validate(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Profile profile = getProfileByUserId(userId);

        updateUserData(request, profile);
        profileRepository.save(profile);
        return createProfileResponse(profile);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProfileResponse getProfile() throws NotFoundException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Profile profile = getProfileByUserId(userId);
        return createProfileResponse(profile);
    }

    private Profile getProfileByUserId(String userId) throws NotFoundException {
        return profileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("Profile not found"));
    }

    private void updateUserData(ProfileRequest request, Profile profile) throws ValidationCustomException {
        updateUsernameIfChange(request.getUsername(), profile);
        updateEmailIfChange(request.getEmail(), profile);
        profile.getUser().setName(request.getName());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setPostcode(request.getPostcode());
    }

    private void updateUsernameIfChange(String newUsername, Profile profile) throws ValidationCustomException {
        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(profile.getUser().getUsername())) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new ValidationCustomException(StatusMessages.USERNAME_BEEN_TAKEN, "username");
            }
            profile.getUser().setUsername(newUsername);
        }
    }

    private void updateEmailIfChange(String newEmail, Profile profile) throws ValidationCustomException {
        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(profile.getUser().getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new ValidationCustomException(StatusMessages.EMAIL_TAKEN, "email");
            }
            profile.getUser().setEmail(newEmail);
        }
    }

    private ProfileResponse createProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .name(profile.getUser().getName())
                .username(profile.getUser().getUsername())
                .email(profile.getUser().getEmail())
                .phone(profile.getPhone())
                .address(profile.getAddress())
                .city(profile.getCity())
                .country(profile.getCountry())
                .postcode(profile.getPostcode())
                .build();
    }
}

