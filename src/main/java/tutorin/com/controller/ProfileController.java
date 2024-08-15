package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.request.profile.ProfileRequest;
import tutorin.com.entities.response.profile.ProfileResponse;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.ProfileService;

@Slf4j
@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_PROFILE)
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Profile API")
public class ProfileController {
    private final Utilities utilities;
    private final ProfileService profileService;

    @Operation(summary = "Update Profile")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<ProfileResponse>> updateProfile(@RequestBody ProfileRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return profileService.updateProfile(request);
            } catch (NotFoundException | ValidationCustomException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_UPDATE);
    }

    @Operation(summary = "Get Profile")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<ProfileResponse>> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authenticated User: {}, Roles: {}", auth.getName(), auth.getAuthorities());
        return utilities.handleRequest(() -> {
            try {
                return profileService.getProfile();
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE);
    }
}
