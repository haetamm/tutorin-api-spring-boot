package tutorin.com.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.UserRoleEnum;
import tutorin.com.entities.user.LoginRequest;
import tutorin.com.entities.user.RegisterRequest;
import tutorin.com.entities.user.LoginResponse;
import tutorin.com.entities.user.RegisterResponse;
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

        Role admin = roleService.saveOrGet(UserRoleEnum.ROLE_ADMIN);

        userRepository.saveAndFlush(User.builder()
                .name(superAdminName)
                .username(superAdminUsername)
                .email(superAdminEmail)
                .password(passwordEncoder.encode(superAdminPassword))
                .roles(List.of(admin))
                .isEnable(true)
                .build());
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
                .username(user.getUsername())
                .token(token)
                .roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
    }

    private RegisterResponse getRegisterResponse(RegisterRequest request, List<Role> roles) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = userRepository.saveAndFlush(User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(hashedPassword)
                .roles(roles)
                .isEnable(true)
                .build());

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
                .username(user.getUsername())
                .roles(rolesName)
                .build();
    }
}
