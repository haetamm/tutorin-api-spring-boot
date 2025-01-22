package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.profile.ProfileRequest;
import tutorin.com.entities.profile.ProfileResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.model.Image;
import tutorin.com.model.Profile;
import tutorin.com.model.Resume;
import tutorin.com.model.User;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.ProfileService;
import tutorin.com.service.UserService;
import tutorin.com.validation.ValidationUtil;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ValidationUtil validationUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProfileResponse updateProfile(ProfileRequest request) throws NotFoundException, ValidationCustomException {
        validationUtil.validate(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUserId(userId);
        updateUserData(request, user);
        return createProfileResponse(user);
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileResponse getProfile() throws NotFoundException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUserId(userId);
        return createProfileResponse(user);
    }

    private void updateUserData(ProfileRequest request, User user) throws ValidationCustomException {
        Profile profile = user.getProfile();
        updateUsernameIfChange(request.getUsername(), user);
        updateEmailIfChange(request.getEmail(), user);
        profile.setName(request.getName());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setCountry(request.getCountry());
        profile.setPostcode(request.getPostcode());
    }

    private void updateUsernameIfChange(String newUsername, User user) throws ValidationCustomException {
        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername())) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new ValidationCustomException(StatusMessages.USERNAME_BEEN_TAKEN, "username");
            }
            user.setUsername(newUsername);
        }
    }

    private void updateEmailIfChange(String newEmail, User user) throws ValidationCustomException {
        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new ValidationCustomException(StatusMessages.EMAIL_BEEN_TAKEN, "email");
            }
            user.setEmail(newEmail);
        }
    }

    private ProfileResponse createProfileResponse(User user) {
        Profile profile = user.getProfile();
        Image image = user.getImage();
        Resume resume = user.getResume();
        return ProfileResponse.builder()
                .name(profile.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(profile.getPhone())
                .address(profile.getAddress())
                .city(profile.getCity())
                .country(profile.getCountry())
                .postcode(profile.getPostcode())
                .image(Utilities.createResponseFile(image))
                .resume(Utilities.createResponseFile(resume))
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }
}

