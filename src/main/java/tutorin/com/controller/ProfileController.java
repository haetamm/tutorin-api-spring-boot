package tutorin.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.image.ImageResponse;
import tutorin.com.entities.profile.ProfileRequest;
import tutorin.com.entities.profile.ProfileResponse;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.ProfileService;

import java.io.IOException;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_PROFILE)
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Profile API")
public class ProfileController {
    private final Utilities utilities;
    private final ProfileService profileService;

    @Operation(summary = "Current user (student and tutor) update profile")
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

    @Operation(summary = "Current user (student and tutor) get profile")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<ProfileResponse>> getProfile() {
        return utilities.handleRequest(() -> {
            try {
                return profileService.getProfile();
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_RETRIEVE);
    }

    @Operation(summary = "Current user upload profile image")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @PutMapping(
            path = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<ImageResponse>> uploadProfile(@RequestParam("image") MultipartFile imageRequest) {
        return utilities.handleRequest(() -> {
            try {
                return profileService.upload(imageRequest);
            } catch (NotFoundException | BadRequestException | IOException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_UPDATE);
    }
}
