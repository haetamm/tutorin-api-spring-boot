package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tutorin.com.constant.StatusMessages;
import tutorin.com.entities.file.FileResponse;
import tutorin.com.entities.user.ChangePasswordRequest;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.exception.ValidationCustomException;
import tutorin.com.helper.Utilities;
import tutorin.com.model.Image;
import tutorin.com.model.Resume;
import tutorin.com.model.User;
import tutorin.com.repository.UserRepository;
import tutorin.com.service.ImageService;
import tutorin.com.service.ResumeService;
import tutorin.com.service.UserService;
import tutorin.com.validation.ValidationUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final ResumeService resumeService;


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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileResponse uploadImage(MultipartFile imageRequest) throws NotFoundException, BadRequestException, IOException {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findById(userId);
        Image image = user.getImage();

        if (image == null) {
            Image imageResult = imageService.save(imageRequest);
            user.setImage(imageResult);
        } else {
            Image changeImage = imageService.updateById(image.getId(), imageRequest);
            user.setImage(changeImage);
        }

        return Utilities.createResponseFile(image);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileResponse uploadResume(MultipartFile imageRequest) throws NotFoundException, BadRequestException, IOException {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = findById(userId);
        Resume resume = user.getResume();

        if (resume == null) {
            Resume resumeResult = resumeService.save(imageRequest);
            user.setResume(resumeResult);
        } else {
            Resume changeResume= resumeService.updateById(resume.getId(), imageRequest);
            user.setResume(changeResume);
        }

        return Utilities.createResponseFile(resume);
    }

    private User findById(String id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(StatusMessages.USER_NOT_FOUND));
    }
}
