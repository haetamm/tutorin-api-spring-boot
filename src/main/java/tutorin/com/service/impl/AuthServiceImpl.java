package tutorin.com.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tutorin.com.constant.UserRoleEnum;
import tutorin.com.entities.user.*;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.model.Profile;
import tutorin.com.model.Role;
import tutorin.com.model.User;
import tutorin.com.repository.ProfileRepository;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.AuthService;
import tutorin.com.service.JwtService;
import tutorin.com.service.RoleService;
import tutorin.com.validation.ValidationUtil;

import java.util.*;
import static org.hibernate.internal.util.collections.CollectionHelper.listOf;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;
    private final ProfileRepository profileRepository;
    private final JavaMailSender mailSender;

    @Value("${tutorin_api.super-admin.name}")
    private String superAdminName;
    @Value("${tutorin_api.super-admin.username}")
    private String superAdminUsername;
    @Value("${tutorin_api.super-admin.email}")
    private String superAdminEmail;
    @Value("${tutorin_api.super-admin.password}")
    private String superAdminPassword;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.redirect.url}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.grant.type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUrl;
    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String googleUserInfoUrl;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        Optional<User> account = userRepository.findByUsername(superAdminUsername);
        if (account.isPresent()) return;
        RegisterRequest superAdminRequest = new RegisterRequest(superAdminName, superAdminUsername, superAdminEmail, superAdminPassword);
        Role admin = roleService.saveOrGet(UserRoleEnum.ROLE_ADMIN);
        saveToUserRepository(superAdminRequest, List.of(admin));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerStudent(RegisterRequest request) {
        validationUtil.validate(request);
        Role role = roleService.saveOrGet(UserRoleEnum.ROLE_STUDENT);
        return getRegisterResponse(request, List.of(role));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerTutor(RegisterRequest request) {
        validationUtil.validate(request);
        Role role = roleService.saveOrGet(UserRoleEnum.ROLE_TUTOR);
        return getRegisterResponse(request, List.of(role));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginResponse login(LoginRequest request) {
        validationUtil.validate(request);
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        User user = (User) authenticate.getPrincipal();
        return getLoginResponse(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String forgetPassword(ForgotPasswordRequest request) throws ValidationCustomException {
        validationUtil.validate(request);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ValidationCustomException("Email not found", "email"));
        String token = jwtService.generateToken(user);
        user.setResetPasswordToken(token);
        userRepository.save(user);
        String subject = "Reset Password";
        String text = String.format("To reset your password, click the link below:\n https://tutorin.netlify.app/reset-password?token=%s", token);
        sendEmail(user.getEmail(), subject, text);

        return "Password reset link sent to your email";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String resetPassword(String token, ResetPasswordRequest request) throws BadRequestException {
        validationUtil.validate(request);
        if (!jwtService.verifyJwtToken(token)) {
            throw new BadRequestException("Invalid or expired token");
        }

        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user.setResetPasswordToken(null);
        return "Password reset successfully, please log in.";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object socialite(String code, String scope) {
        HttpEntity<MultiValueMap<String, String>> requestEntity = null;
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = getStringStringMultiValueMap(code, scope);

            requestEntity = new HttpEntity<>(params, httpHeaders);
            String tokenUrl = googleTokenUrl;
            GoogleTokenResponse tokenResponse = restTemplate.postForObject(tokenUrl, requestEntity, GoogleTokenResponse.class);

            assert tokenResponse != null;
            GoogleUserInfo userInfo = getProfileDetailsGoogle(tokenResponse.getAccess_token());

            Optional<User> optionalUser = userRepository.findByEmail(userInfo.getEmail());
            User user = optionalUser.orElse(null);

            if (user == null) {
                userInfo.setUsername(userInfo.getEmail().split("@")[0]);
                userInfo.setTokenAccess(tokenResponse.getAccess_token());
                return userInfo;
            }
            return getLoginResponse(user);

        } catch (HttpClientErrorException e) {
            System.err.println("Google login error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            assert requestEntity != null;
            System.err.println("Request Headers: " + requestEntity.getHeaders());
            System.err.println("Request Body: " + requestEntity.getBody());
            throw new RuntimeException("Error during Google login", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during Google login", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginResponse regUserWithGoogle(RegisterWithGoogleRequest request) {
        validationUtil.validate(request);
        GoogleUserInfo userInfo = getProfileDetailsGoogle(request.getTokenAccess());
        UserRoleEnum roleEnum;
        roleEnum = UserRoleEnum.valueOf(request.getRole().toUpperCase());
        Role roles = roleService.saveOrGet(roleEnum);
        RegisterRequest tempRegisterRequest = new RegisterRequest(
                userInfo.getName(),
                request.getUsername(),
                userInfo.getEmail(),
                Utilities.generateRandomString(8)
        );
        User user = saveToUserRepository(tempRegisterRequest, listOf(roles));
        saveToProfileRepository(user);
        return getLoginResponse(user);
    }

    private GoogleUserInfo getProfileDetailsGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        String url = googleUserInfoUrl;
        ResponseEntity<GoogleUserInfo> userInfo = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GoogleUserInfo.class);
        return userInfo.getBody();
    }

    private MultiValueMap<String, String> getStringStringMultiValueMap(String code, String scope) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId.trim());
        params.add("client_secret", googleClientSecret.trim());
        params.add("redirect_uri", googleRedirectUri.trim());
        params.add("scope", scope);
        params.add("grant_type", grantType.trim());
        return params;
    }

    private LoginResponse getLoginResponse(User user) {
        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .token(token)
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private User saveToUserRepository(RegisterRequest user, List<Role> roles) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        return userRepository.saveAndFlush(User.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(hashedPassword)
                .roles(roles)
                .isEnable(true)
                .build());
    }

    private RegisterResponse getRegisterResponse(RegisterRequest request, List<Role> roles) {
        User user = saveToUserRepository(request, roles);
        saveToProfileRepository(user);

        List<String> rolesName = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();

        return RegisterResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .roles(rolesName)
                .build();
    }

    private void saveToProfileRepository(User user) {
        Profile profile = Profile.builder()
                .user(user)
                .phone("")
                .address("")
                .city("")
                .country("")
                .postcode("")
                .build();
        profileRepository.save(profile);
    }

    private void sendEmail(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
