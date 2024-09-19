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
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.user.*;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.AuthService;
import tutorin.com.service.UserService;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_USER)
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;
    private final Utilities utilities;

    @Operation(summary = "Register Student")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_TUTOR')")
    @PostMapping(path = "/secure", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> changePassword(@RequestBody ChangePasswordRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return userService.changePassword(request);
            } catch (NotFoundException | ValidationCustomException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, StatusMessages.SUCCESS_UPDATE);
    }
}
