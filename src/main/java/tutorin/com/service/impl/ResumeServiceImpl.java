package tutorin.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tutorin.com.constant.StatusMessages;
import tutorin.com.constant.UserRoleEnum;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Resume;
import tutorin.com.model.Role;
import tutorin.com.model.User;
import tutorin.com.repository.JobApplicationRepository;
import tutorin.com.repository.ResumeRepository;
import tutorin.com.service.ResumeService;
import tutorin.com.service.UserService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final JobApplicationRepository jobApplicationRepository;
    @Lazy
    @Autowired
    private UserService userService;

    @Value("${tutorin_api.resume.path}")
    private String pathResume;

    private Path resumePath;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resume save(MultipartFile resume) throws BadRequestException, IOException {
        resumePath = Paths.get(pathResume);
        String fileName = validateAndSaveResume(resume);
        Path filePath = resumePath.resolve(fileName);

        Resume saved = Resume.builder()
                .name(fileName)
                .path(filePath.toString())
                .size(resume.getSize())
                .contentType(resume.getContentType())
                .build();
        return resumeRepository.saveAndFlush(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resource getById(String id, String jobId, String tutorId) throws NotFoundException, MalformedURLException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getByUserId(userId);
        List<Role> userRoles = user.getRoles();

        boolean isTutor = userRoles.stream()
                .anyMatch(role -> role.getRole() == UserRoleEnum.ROLE_TUTOR);

        boolean isStudent = userRoles.stream()
                .anyMatch(role -> role.getRole() == UserRoleEnum.ROLE_STUDENT);

        if (isTutor) {
            if (user.getResume() == null) {
                throw new NotFoundException(StatusMessages.NOT_FOUND);
            } else if (!user.getResume().getId().equals(id)) {
                throw new AccessDeniedException(StatusMessages.ACCESS_DENIED);
            }
        } else if (isStudent) {
            jobApplicationRepository.findByJobIdAndTutorIdAndStudentId(jobId, tutorId, userId)
                    .orElseThrow(() -> new AccessDeniedException(StatusMessages.ACCESS_DENIED));
        }

        Resume resume = findById(id);
        Path filePath = Paths.get(resume.getPath());

        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new NotFoundException("File not found or not readable: " + resume.getPath());
        }

        return new UrlResource(filePath.toUri());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Resume updateById(String id, MultipartFile resume) throws NotFoundException, IOException, BadRequestException {
        Resume resumeResult = findById(id);
        Path filePath = Paths.get(resumeResult.getPath());

        Files.delete(filePath);

        String newFileName = validateAndSaveResume(resume);
        Path newFilePath = resumePath.resolve(newFileName);

        resumeResult.setName(newFileName);
        resumeResult.setPath(newFilePath.toString());
        resumeResult.setSize(resume.getSize());
        resumeResult.setContentType(resume.getContentType());

        return resumeResult;
    }

    private String validateAndSaveResume(MultipartFile resume) throws IOException, BadRequestException {
        List<String> allowedContentTypes = List.of("application/pdf");
        if (!allowedContentTypes.contains(resume.getContentType())) {
            throw new BadRequestException("Invalid file type. Only PDF is allowed.");
        }

        long maxFileSize = 512_000; // 500KB in bytes
        if (resume.getSize() > maxFileSize) {
            throw new BadRequestException("File size exceeds the maximum limit of 500KB.");
        }

        String fileName = System.currentTimeMillis() + resume.getOriginalFilename();
        Path filePath = resumePath.resolve(fileName);
        Files.copy(resume.getInputStream(), filePath);

        return fileName;
    }

    private Resume findById(String id) throws NotFoundException {
        return resumeRepository.findById(id).orElseThrow(() -> new NotFoundException(StatusMessages.NOT_FOUND));
    }
}
