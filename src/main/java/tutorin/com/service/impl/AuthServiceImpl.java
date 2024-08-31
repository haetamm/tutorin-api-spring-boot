package tutorin.com.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import tutorin.com.constant.StatusMessages;
import tutorin.com.constant.UserRoleEnum;
import tutorin.com.entities.user.*;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.model.Profile;
import tutorin.com.model.Role;
import tutorin.com.model.User;
import tutorin.com.repository.ProfileRepository;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.AuthService;
import tutorin.com.service.JwtService;
import tutorin.com.service.RoleService;
import tutorin.com.validation.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
        String token = jwtService.generateToken(user);
        return LoginResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .token(token)
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
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

        Profile profile = Profile.builder()
                .user(user)
                .phone("")
                .address("")
                .city("")
                .country("")
                .postcode("")
                .build();
        profileRepository.save(profile);

        List<String> rolesName = user.getRoles().stream()
                .map(role -> role.getRole().name())
                .toList();

        return RegisterResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .roles(rolesName)
                .build();
    }

    private void sendEmail(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
