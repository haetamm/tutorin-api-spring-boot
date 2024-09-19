package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.user.ChangePasswordRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.model.User;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.UserService;
import tutorin.com.validation.ValidationUtil;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @Override
    public User getByUserId(String id) throws NotFoundException {
        return findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String changePassword(ChangePasswordRequest request) throws NotFoundException, ValidationCustomException {
        validationUtil.validate(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findById(userId);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationCustomException("Password incorrect", "password");
        }
        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return "Password changed successfully";
    }

    private User findById(String id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(StatusMessages.USER_NOT_FOUND));
    }
}
