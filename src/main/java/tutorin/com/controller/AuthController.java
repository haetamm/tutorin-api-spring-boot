package tutorin.com.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tutorin.com.constant.ApiUrl;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.WebResponse;
import tutorin.com.entities.request.user.LoginRequest;
import tutorin.com.entities.request.user.RegisterRequest;
import tutorin.com.entities.response.user.LoginResponse;
import tutorin.com.entities.response.user.RegisterResponse;
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
}
