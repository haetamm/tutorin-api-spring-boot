package tutorin.com.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import tutorin.com.exception.BadRequestException;
import tutorin.com.exception.NotFoundException;
import tutorin.com.model.Image;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ImageService {
    Image save(MultipartFile image) throws BadRequestException, IOException;
    Resource getById(String id) throws NotFoundException, MalformedURLException;
    Image updateById(String id, MultipartFile image) throws NotFoundException, IOException, BadRequestException;
}
