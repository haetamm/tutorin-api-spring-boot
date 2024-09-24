package tutorin.com.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Resume;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ResumeService {
    Resume save(MultipartFile resume) throws BadRequestException, IOException;
    Resource getById(String id, String jobId, String tutorId) throws NotFoundException, MalformedURLException;
    Resume updateById(String id, MultipartFile resume) throws NotFoundException, IOException, BadRequestException;
}
