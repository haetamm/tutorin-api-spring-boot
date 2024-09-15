package tutorin.com.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.user.*;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.service.AuthService;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.API_AUTH)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {
    private final AuthService authService;
    private final Utilities utilities;

    @Operation(summary = "Register Student")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/register/student", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<RegisterResponse>> regStudent(@RequestBody RegisterRequest request) {
        return utilities.handleRequest(() -> authService.registerStudent(request), HttpStatus.CREATED, "Account registered");
    }

    @Operation(summary = "Register Tutor")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/register/tutor", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<RegisterResponse>> regTutor(@RequestBody RegisterRequest request) {
        return utilities.handleRequest(() -> authService.registerTutor(request), HttpStatus.CREATED, "Account registered");
    }

    @Operation(summary = "Login")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return utilities.handleRequest(() -> authService.login(request), HttpStatus.OK, StatusMessages.SUCCESS_LOGIN);
    }

    @Operation(summary = "Register User")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/register/with-google", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<LoginResponse>> regUserWithGoogle(@RequestBody RegisterWithGoogleRequest request) {
        return utilities.handleRequest(() -> authService.regUserWithGoogle(request), HttpStatus.OK, StatusMessages.SUCCESS_LOGIN);
    }

    @Operation(summary = "Login With Google")
    @SecurityRequirement(name = "Authorization")
    @GetMapping(path = "/socialite", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<Object>> loginWithGoogle(
            @RequestParam("code") String code,
            @RequestParam("scope") String scope
    ) {
        return utilities.handleRequest(() -> authService.socialite(code, scope), HttpStatus.OK, StatusMessages.SUCCESS_LOGIN);
    }

    @Operation(summary = "User forgot password")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/forgot-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return authService.forgetPassword(request);
            } catch (ValidationCustomException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, "Success");
    }

    @Operation(summary = "User reset password")
    @SecurityRequirement(name = "Authorization")
    @PostMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<String>> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest request) {
        return utilities.handleRequest(() -> {
            try {
                return authService.resetPassword(token, request);
            } catch (ValidationCustomException | BadRequestException e) {
                throw new RuntimeException(e);
            }
        }, HttpStatus.OK, "Success");
    }
}
